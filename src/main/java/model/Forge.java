package model;

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
    public void configEntidades(List<Node> listaEntidades) {

        for (Node nodeDom : listaEntidades) {
            this.script += "jpa-new-entity --named " + nodeDom.getNome() + "\n";
            HashMap<String, String> prop = nodeDom.getPropriedades();
            Set<String> chaves = prop.keySet();
            for (String chave : chaves) {
                String field = new String();
                field = "jpa-new-field --named " + chave;

                if (TiposBasicosForge.temTraducao(prop.get(chave)) == null) {
                    // TODO Tratar outros relacionamentos
                    field += " --type org." + this.nomeProjeto + ".model."
                            + prop.get(chave) + " --relationshipType "
                            + "One-to-Many\n";
                } else {
                    field += " --type " + prop.get(chave) + "\n";
                }

                field += "constraint-add --constraint NotNull --onProperty "
                        + chave + "\n";
                script += field;
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
