package fr.surli.test.jdtmodule;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AnalyzeWithJDT {
	public static void main(String[] args) throws IOException {
		File pathWithModule = new File("./src/main/resources/mymodule");
		File pathWithoutModule = new File("./src/main/java");

		File path = pathWithModule;

		List<String> pathes = Files.walk(path.toPath()).filter(Files::isRegularFile).map(path1 -> {return path1.toFile().getAbsolutePath();}).collect(Collectors.toList());
        List<String> argsJdt = new ArrayList<>();

        argsJdt.addAll(Arrays.asList("-encoding",
                "UTF-8",
                "-1.9",
                "-preserveAllLocals",
                "-noExit",
                "-enableJavadoc"));

		Collections.reverse(pathes);
        argsJdt.addAll(pathes);

		// configure the Main from JDT with the right arguments
		CustomMain main = new CustomMain(System.out, System.err);
		main.configure(argsJdt.toArray(new String[0]));

		// get the units: the errors come in that method
		// from the call to CustomCompiler
		CompilationUnitDeclaration[] compilationUnits = main.getUnits();

		for (CompilationUnitDeclaration compilationUnit : compilationUnits) {
			CategorizedProblem[] errors = compilationUnit.compilationResult().getErrors();

			if (errors != null && errors.length > 0) {
				System.out.println("Errors for "+ new String(compilationUnit.getFileName()));

				for (CategorizedProblem error : errors) {
					System.out.println(error);
				}

			}
		}

	}
}
