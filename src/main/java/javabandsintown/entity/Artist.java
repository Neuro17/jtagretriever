package javabandsintown.entity;

public class Artist {
	
	private String id;

	private String name;
	
	public Artist(){
	}
	
	public Artist(String name, String id){
		this.name = name;
		this.id = id;
	}
	public Artist(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Bandsintown artist [name=" + name + ", id=" + id + "]";
	}

}
