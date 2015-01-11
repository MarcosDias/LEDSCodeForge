package ctrl;

import model.ClassDiagram;
import model.Forge;
import model.Specification;
import model.SuperClass;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Marcos Dias
 */
public class ForgeController {

    public void createProject(Specification specification) throws IOException {
        Forge forge = new Forge();
                        forge.criarProjeto(specification.getName(), "br.edu.leds.ledscode");
        forge.configBanco(specification.getFeature().getOrm(), specification.getFeature().getDatabase());
        // TODO Enuns nao foram implementados
        ClassDiagram diagram = specification.getClassDiagram();
        ArrayList<SuperClass> classesModel = diagram.getClasses();

        if (specification.getFeature().getApplicationType() == "WEB") {
            forge.configEntidades(classesModel);
            forge.gerarInterfaceWeb();
            forge.configRest();
            forge.build();
            forge.exit();
        }

        forge.print(Forge.ARQUIVO, Forge.EXTENSAO);
        forge.print(Forge.TERMINAL, null);
    }
}
