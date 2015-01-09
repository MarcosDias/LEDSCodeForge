package model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

@Getter
@Setter
public class Forge extends BaseFramework {
    public final static String EXTENSAO = "forge";


    /**
     * Etapa inicial para criação do projeto
     *
     * @param nome   - nome do projeto
     * @param pacote - nome da pacote do projeto
     */
    public void criarProjeto(String nome, String pacote) {
        this.nomeProjeto = nome;

        this.script += "project-new --named " + nome + " --type war"
                + "--targetLocation " + pacote + "\n";
        // Subentende que o top level package esteja correto
        if (pacote == null)
            this.script += " --topLevelPackage " + pacote + "\n\n";
    }

    /**
     * Configuracoes do banco de dados
     *
     * @param provider - Define o ORM
     * @param dbType   - Defino o Banco de Dados
     */
    public void configBanco(String provider, DbType dbType) {
        this.script += "jpa-setup --provider " + provider + " --dbType "
                + dbType + "\n\n";
    }

    /**
     * Cria as entidades que serao persistidas no banco de dados
     *
     * @param listaEntidades - Lista de entidades ja mapeadas para os no do grafo
     */
    public void configEntidades(ArrayList<SuperClass> listaEntidades) {

        // TODO - Enum nao implementado
        for (SuperClass classModel : listaEntidades) {
            if (classModel instanceof model.Class) {
                this.script += "jpa-new-entity --named " + classModel.getName() + "\n";
                
                for (Attribute attr : ((Class) classModel).getAttributes()){
                    String field = "";
                    field = "jpa-new-field --named " + attr.getName();
                    
                    if(attr.getType() instanceof PrimitiveType){
                        PrimitiveType typeAttribute = (PrimitiveType) attr.getType();
                        
                        String traducao = TiposBasicosForge.temTraducao(typeAttribute.getType().name());
                        
                        field += " --type " + traducao + "\n";
                        
                    } else if (attr.getType() instanceof model.Class){
                        model.Class traducao = (Class) attr.getType();
                        field += " --type org." + this.nomeProjeto + ".model."
                                + traducao.getName() + " --relationshipType "
                                + "One-to-Many\n";
                    }

                    field += "constraint-add --constraint NotNull --onProperty "
                            + attr.getName() + "\n";
                    script += field;
                }
            }
        }
        this.script += "\n";
    }

    /**
     * Comando que gera toda a da parte web
     */
    public void gerarInterfaceWeb() {
        this.script += "faces-setup --facesVersion 2.2\n";
        this.script += "scaffold-setup\n";
        this.script += "scaffold-generate --targets org." + this.nomeProjeto
                + ".model.*\n\n";

    }

    /**
     * Configuracoes para criar um web service
     */
    public void configRest() {
        this.script += "rest-setup\n";
        this.script += "rest-generate-endpoints-from-entities "
                + "--targets org." + this.nomeProjeto
                + ".model.* --contentType application/xml\n\n";
    }

    /**
     * Compila o projeto
     */
    public void build() {
        this.script += "build\n\n";
    }

    /**
     * Comando para sair do terminal do forge
     */
    public void exit() {
        this.script += "quit\n\n";
    }

    public Forge() {
        super();
    }
}
