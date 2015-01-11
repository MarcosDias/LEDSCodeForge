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
        forge.configEntidades(classesModel);

        if (specification.getFeature().getApplicationType().equals("WEB")) {
            forge.gerarInterfaceWeb();
            forge.configRest();
        }
        forge.build();
        forge.exit();

        forge.print(Forge.ARQUIVO, Forge.EXTENSAO);
//        forge.print(Forge.TERMINAL, null);
    }
}
