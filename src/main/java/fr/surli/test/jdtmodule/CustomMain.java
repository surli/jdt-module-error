package fr.surli.test.jdtmodule;

import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.batch.Main;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;

import java.io.OutputStream;
import java.io.PrintWriter;

public class CustomMain extends Main {
    public CustomMain(OutputStream outWriter, OutputStream errWriter) {
        super(new PrintWriter(outWriter), new PrintWriter(errWriter), false, null, null);
    }

    public CompilationUnitDeclaration[] getUnits() {
        startTime = System.currentTimeMillis();
        INameEnvironment environment = getLibraryAccess();
        CompilerOptions compilerOptions = new CompilerOptions(this.options);

        IErrorHandlingPolicy errorHandlingPolicy = new IErrorHandlingPolicy() {
            public boolean proceedOnErrors() {
                return false;
            }

            // we wait for all errors to be gathered before stopping
            public boolean stopOnFirstError() {
                return false;
            }

            public boolean ignoreAllErrors() {
                return false;
            }
        };

        IProblemFactory problemFactory = getProblemFactory();

        CustomCompiler customCompiler = new CustomCompiler(
                environment, errorHandlingPolicy, compilerOptions,
                new DumbRequestor(), problemFactory, this.out, null);

        final CompilationUnitDeclaration[] result = customCompiler.buildUnits(getCompilationUnits());

        return result;
    }
}
