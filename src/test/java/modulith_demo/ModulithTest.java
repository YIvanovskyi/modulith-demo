package modulith_demo;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class ModulithTest {

    @Test
    void verifyModularStructure() {
        ApplicationModules modules = ApplicationModules.of(ModulithDemoApplication.class);
        modules.verify();
    }

    @Test
    void generateDocumentation() {
        ApplicationModules modules = ApplicationModules.of(ModulithDemoApplication.class);
        new Documenter(modules)
            .writeDocumentation()
            .writeIndividualModulesAsPlantUml();
    }
}
