import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TestEnum {
	public static final String json = "{\"status\":\"1\"}";
	public static final String Staus = "1";
	public static final long count = 10000L;
	
	public static void main(String[] args) {
		
		long start = System.currentTimeMillis();
		Data data = null;
		for(int i=0; i< count; ++i){
			data = new Gson().fromJson(json, Data.class);
			EStatus status = data.status;
			boolean result = status == EStatus.OFF;
			result = Staus.equalsIgnoreCase(status.name);
		}
		long start1 = System.currentTimeMillis();
		System.err.println("EStatus result:" + (start1 - start));
		
		
		start = System.currentTimeMillis();
		Data1 data1 = null;
		for(int i=0; i< count; ++i){
			data1 = new Gson().fromJson(json, Data1.class);
			String status = data1.status;
			boolean result = Staus.equalsIgnoreCase(status);
		}
		start1 = System.currentTimeMillis();
		System.err.println("String result:" + (start1 - start));
	}
	
	public static class Data {
		EStatus status;
	}

	public static class Data1 {
		@Expose
		String status;
	}

	public static enum EStatus {
		@SerializedName("1")
		ON("1", 1),
		@SerializedName("2")
		OFF("2", 2),
		@SerializedName("3")
		OFF3("3", 3),
		@SerializedName("4")
		OFF4("4", 2),
		@SerializedName("5")
		OFF5("5", 2);
		
		public String name;
		public int code;
		
		private EStatus(String name, int code){
			this.name = name;
			this.code = code;
		}
	}
}


