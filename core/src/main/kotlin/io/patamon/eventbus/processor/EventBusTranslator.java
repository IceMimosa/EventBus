package io.patamon.eventbus.processor;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Pair;
import io.patamon.eventbus.core.EventBusHandler;
import io.patamon.eventbus.core.Subscribe;

/**
 * Desc: AST 处理
 *
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/11/5
 */
public class EventBusTranslator extends TreeTranslator {

    private TreeMaker treeMaker;
    private MakerHelper makerHelper;

    /**
     * 存放要调用的方法信息
     *
     * 方法名 -> 参数类型全名
     */
    List<Pair<String, String>> methods = List.nil();

    public EventBusTranslator(TreeMaker treeMaker, MakerHelper makerHelper) {
        this.treeMaker = treeMaker;
        this.makerHelper = makerHelper;
    }

    /**
     * 遍历到类的时候执行
     */
    @Override
    public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
        super.visitClassDef(jcClassDecl);
        // 1. 实现 EventBusHandler 接口
        for (JCTree.JCExpression implementing : jcClassDecl.implementing) {
            if (EventBusHandler.class.getName().equals(implementing.type.toString())) {
                return;
            }
        }
        if (!methods.isEmpty()) {
            jcClassDecl.implementing = jcClassDecl.implementing.append(makerHelper.makeInterface(EventBusHandler.class));
            // 2. 实现类的方法
            jcClassDecl.defs = jcClassDecl.defs.append(createInvokeMethod());
            this.result = jcClassDecl;
            // makerHelper.logInfo(jcClassDecl.toString());
        }
    }

    /**
     * 遍历所有方法
     */
    @Override
    public void visitMethodDef(JCTree.JCMethodDecl jcMethodDecl) {
        super.visitMethodDef(jcMethodDecl);
        // 1. 获取注解
        List<JCTree.JCAnnotation> annotations = jcMethodDecl.getModifiers().getAnnotations();
        if (annotations == null || annotations.size() <= 0) {
            return;
        }
        // 2. 获取方法参数
        List<JCTree.JCVariableDecl> parameters = jcMethodDecl.getParameters();
        if (parameters == null || parameters.size() != 1) {
            return;
        }
        for (JCTree.JCAnnotation annotation : annotations) {
            if (Subscribe.class.getName().equals(annotation.type.toString())) {
                this.methods = methods.append(new Pair<>(jcMethodDecl.name.toString(), parameters.head.getType().type.toString()));
            }
        }
    }

    @Override
    public void visitVarDef(JCTree.JCVariableDecl jcVariableDecl) {
        super.visitVarDef(jcVariableDecl);
    }

    /**
     * 创建 EventBusHandler 的方法
     */
    private JCTree.JCMethodDecl createInvokeMethod() {
        // 构建方法参数
        JCTree.JCVariableDecl param1 = treeMaker.VarDef(
                treeMaker.Modifiers(Flags.PARAMETER), makerHelper.makeName("type"), treeMaker.Ident(makerHelper.makeSymbol(String.class)), null
        );
        JCTree.JCVariableDecl param2 = treeMaker.VarDef(
                treeMaker.Modifiers(Flags.PARAMETER), makerHelper.makeName("arg"), treeMaker.Ident(makerHelper.makeSymbol(Object.class)), null
        );

        JCTree.JCMethodDecl method = treeMaker.MethodDef(
                // public方法
                treeMaker.Modifiers(Flags.PUBLIC),
                // 方法名称
                makerHelper.makeName("$$__invoke__$$"),
                // 方法返回的类型, void
                treeMaker.Type(new Type.JCVoidType()),
                // 泛型参数
                List.nil(),
                // 方法参数
                List.of(param1, param2),
                // throw表达式
                List.nil(),
                // 方法体
                createInvokeMethodBody(),
                // 默认值
                null
        );
        // 增加 Override 注解
        method.getModifiers().annotations = method.getModifiers().annotations.append(
                makerHelper.makeAnnotation(Override.class)
        );
        return method;
    }

    /**
     * 创建执行方法体
     */
    private JCTree.JCBlock createInvokeMethodBody() {
        List<JCTree.JCStatement> iFs = List.nil();
        for (Pair<String, String> method: methods) {
            String type = method.fst + "@" + method.snd;
            // if 语句 ->  if ("方法名@参数全名".equals(type))
            JCTree.JCMethodInvocation iF = treeMaker.Apply(
                    List.nil(),
                    treeMaker.Select(treeMaker.Literal(type), makerHelper.makeName("equals")),
                    List.of(treeMaker.Ident(makerHelper.makeName("type")))
            );
            // 执行语句 -> this.method((arg_type) arg)
            JCTree.JCStatement call = treeMaker.Exec(
                    treeMaker.Apply(
                            List.nil(),
                            treeMaker.Select(treeMaker.Ident(makerHelper.makeName("this")), makerHelper.makeName(method.fst)),
                            List.of(treeMaker.TypeCast(makerHelper.makeSymbol(method.snd).type, treeMaker.Ident(makerHelper.makeName("arg"))))
                    )
            );
            iFs = iFs.append(treeMaker.If(iF, call, null));
        }
        return treeMaker.Block(0L, iFs);
    }
}
