package model;

import lombok.Getter;

@Getter
public enum TiposBasicosForge {
	BYTE("byte"),
	INT("int"),
	STRING("String"),
	FLOAT("float"),
	LONG("long"),
	DATE("java.util.Date"),
	CHAR("char"),
	SHORT("short"),
	ENTITY(null),  
	DOUBLE("double"),
	BOOLEAN("boolean"),
	// Extra
	ENUM("String");
	
	private final String nome;

	private TiposBasicosForge(String nome) {
		this.nome = nome;
	}
	
	/**
	 * Busca se existe um tipo, dentro das possibilidades mapeadas no enum
	 * 
	 * @param tipo
	 * @return
	 */
	public static String temTraducao(String tipo) {
		for (TiposBasicosForge basico : TiposBasicosForge.values()) {
			if (basico.getNome() != null && basico.getNome().equals(tipo)) {
				return basico.getNome();
			}
		}
		return null;
	}
}
