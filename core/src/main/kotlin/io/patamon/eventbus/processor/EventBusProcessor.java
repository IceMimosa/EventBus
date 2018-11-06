package io.patamon.eventbus.processor;

import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;
import io.patamon.eventbus.core.EventBusHandler;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Desc: EventBus 对 {@link io.patamon.eventbus.core.Subscribe} 注解处理
 * <p>
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/11/5
 */
@SupportedAnnotationTypes({"io.patamon.eventbus.core.Subscribe"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class EventBusProcessor extends AbstractProcessor {

    /**
     * 语法树
     */
    private Trees trees;

    /**
     * 树节点创建工具类
     */
    private TreeMaker treeMaker;

    private JavacElements javacElements;

    /**
     * 命名工具类
     */
    private Names names;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.trees = Trees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.javacElements = JavacElements.instance(context);
        this.names = Names.instance(context);
    }

    /**
     *
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            for (Element element : roundEnv.getRootElements()) {
                if (element.getKind().isClass()) {
                    // 获取语法树
                    JCTree tree = (JCTree) trees.getTree(element);
                    // 使用TreeTranslator遍历
                    EventBusTranslator translator = new EventBusTranslator(treeMaker, names, messager, javacElements);;
                    tree.accept(translator);

                    if (!translator.methods.isEmpty()) {
                        TreePath path = trees.getPath(element);
                        JCTree.JCCompilationUnit compilationUnit = ((JCTree.JCCompilationUnit) path.getCompilationUnit());
                        List<String> collect = compilationUnit.defs.stream().filter(it -> it instanceof JCTree.JCImport).map(JCTree::toString).collect(Collectors.toList());
                        if (!collect.contains("import io.patamon.eventbus.core.EventBusHandler;")) {
                            compilationUnit.defs = compilationUnit.defs.prepend(treeMaker.Import(
                                    treeMaker.Select(treeMaker.Ident(names.fromString(javacElements.getTypeElement(EventBusHandler.class.getName()).owner.toString())), names.fromString(EventBusHandler.class.getSimpleName())),
                                    false));
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * If the processor class is annotated with {@link
     * SupportedSourceVersion}, return the source version in the
     * annotation.  If the class is not so annotated, {@link
     * SourceVersion#RELEASE_6} is returned.
     *
     * @return the latest source version supported by this processor
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
