package io.patamon.eventbus.processor;

import com.sun.tools.javac.code.Attribute
import com.sun.tools.javac.model.JavacElements
import com.sun.tools.javac.tree.JCTree
import com.sun.tools.javac.tree.TreeMaker
import com.sun.tools.javac.util.List
import com.sun.tools.javac.util.Names
import io.patamon.eventbus.core.EventBusHandler
import javax.annotation.processing.Messager
import javax.tools.Diagnostic

/**
 * Desc: 帮助类
 * <p>
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/11/6
 */
class MakerHelper(
        val treeMaker: TreeMaker,
        val names: Names,
        val javacElements: JavacElements,
        val messager: Messager
) {

    /**
     * 创建类类型 [com.sun.tools.javac.code.Symbol.ClassSymbol]
     */
    fun makeSymbol(t: Class<*>) = javacElements.getTypeElement(t.name)
    fun makeSymbol(t: String) = javacElements.getTypeElement(t)

    /**
     * 创建 Name
     */
    fun makeName(n: String) = names.fromString(n)

    /**
     * 创建接口类型
     */
    fun makeInterface(t: Class<*>) = treeMaker.Ident(makeSymbol(t))

    /**
     * 创建注解类型
     */
    fun makeAnnotation(t: Class<*>) = treeMaker.Annotation(Attribute.Compound(makeSymbol(t).type, List.nil()))

    /**
     * 创建 Import 语句
     */
    fun makeImport(t: Class<*>, static: Boolean = false): JCTree.JCImport {
        val symbol = makeSymbol(EventBusHandler::class.java)
        return treeMaker.Import(
                treeMaker.Select(treeMaker.Ident(makeName(symbol.owner.toString())), symbol.simpleName),
                static
        )
    }

    /**
     * 打印日志
     */
    fun logInfo(s: String) = messager.printMessage(Diagnostic.Kind.NOTE, s)
    fun logWarn(s: String) = messager.printMessage(Diagnostic.Kind.WARNING, s)
    fun logError(s: String) = messager.printMessage(Diagnostic.Kind.ERROR, s)
}
