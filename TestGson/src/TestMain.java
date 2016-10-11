import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang3a.tuple.MutablePair;
import org.apache.commons.lang3a.tuple.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedHashTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;


public class TestMain {

	
    final static Collection nuCon = new Vector();

    static {
        nuCon.add(null);
    }

	public static void main(String[] args) {
	
		System.err.println(true == json2Object("1e1", Boolean.class, false));
		
		System.err.println(filterChinese("为跌幅为·!"));
		
		Date maxDate = new Date();
		maxDate.setTime(Integer.MAX_VALUE);
	
		System.err.println(DateFormat.getDateInstance().format(maxDate));
		
		System.err.println(System.currentTimeMillis() > Integer.MAX_VALUE);
		
		
        String json = "[{\n" +
        "      \"title\": \"统一管理信用卡、水电煤、分期各种账单\",\n" +
        "      \"imageUrl\": \"http://cp.vcredit.com/banner.png\",\n" +
        "      \"pageUrl\": \"http://cp.vcredit.com/news\"\n" +
        "    },,{\n" +
        "      \"title\": \"没钱还款怎么办？免息产品帮你还\",\n" +
        "      \"imageUrl\": \"http://cp.vcredit.com/banner2.png\",\n" +
        "      \"pageUrl\": \"http://cp.vcredit.com/news2\"\n" +
        "    }]";
        
        List<AdvertInfo> advertInfos = json2List(json, new TypeToken<List<AdvertInfo>>(){});
        advertInfos.removeAll(nuCon);
        
        for (AdvertInfo advertInfo : advertInfos) {
			System.err.println(advertInfo.imageUrl);
		}
        
        String json1 = "";
        
        
        MutablePair<String, String> pair = MutablePair.of("jiansheyinghang", "建设银行");
        System.out.println(pair.toString("key:%1$s, value:%2$s"));
       
        
        // With adapter
        final Gson gson = new GsonBuilder().create();
        json1 = gson.toJson(pair);
        System.out.println(json1);
        System.err.println(gson.fromJson(json1, new TypeToken<MutablePair>(){}.getType()));
        
        // With adapter
        final Gson gson1 = new GsonBuilder().registerTypeAdapter(MutablePair.class,
                new PairAdapter().nullSafe()).create();
        json1 = gson1.toJson(pair, new TypeToken<MutablePair>(){}.getType());
        System.out.println(json1);
        System.err.println(gson1.fromJson(json1, new TypeToken<MutablePair>(){}.getType()));

        // With serializer
        final Gson gson2 = new GsonBuilder().registerTypeAdapter(Pair.class,
                new PairSerializer()).create();
        json1 = gson2.toJson(pair, new TypeToken<MutablePair>(){}.getType());
        System.out.println(json1);
        System.err.println(gson2.fromJson(json1, new TypeToken<MutablePair>(){}.getType()));
        
        MutablePair<String, String> pair1 = MutablePair.of("jiansheyinghang", "建设银行");
        // With PairTypeAdapterFactory
        final Gson gson3 = new GsonBuilder().registerTypeAdapterFactory(new PairTypeAdapterFactory()).create();
        json1 = gson3.toJson(pair1);
        System.out.println(json1);
        System.err.println(gson3.fromJson(json1, new TypeToken<MutablePair<String, String>>(){}.getType()));
        
	}
	
    /**
     * 将json转化为list对象
     * @param jsonString  json字符串
     * @param typeToken  对应的类
     * @return
     */
	public static <T> List<T> json2List(String jsonString, TypeToken<List<T>> typeToken) {
        List<T> list = new ArrayList<>();
        try {
            GsonBuilder builder = new GsonBuilder();
            // 不转换没有 @Expose 注解的字段
            builder.excludeFieldsWithoutExposeAnnotation();
            Gson gson = builder.create();
            List<T> ts = gson.fromJson(jsonString, typeToken.getType());
            list.addAll(ts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
	
	 public static <T> T json2Object(String jsonString, Class<T> cls) {
	        return json2Object(jsonString, cls, null);
	    }
	
	 public static <T> T json2Object(String jsonString, Class<T> cls, T defaultValue) {
	        T t = defaultValue;
	        try {
	            GsonBuilder builder = new GsonBuilder();
	            // 不转换没有 @Expose 注解的字段
	            builder.excludeFieldsWithoutExposeAnnotation();
	            Gson gson = builder.create();
	            t = gson.fromJson(jsonString, cls);
	        } catch (Exception e) {
	            
	        }
	        return t;
	    }
	 
	 public static String filterChinese(String chin)
	 {
	 	chin = chin.replaceAll("[^(·\\u4e00-\\u9fa5)]", "");
	 	return chin;
	 }
}

class AdvertInfo implements Serializable {
    @Expose
    protected String title;

    @Expose
    protected String imageUrl;

    @Expose
    protected String pageUrl;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
    
    
}

class PairAdapter extends TypeAdapter<Pair> {
    final Gson embedded = new Gson();

    public PairAdapter() {
        super();
    }

    @Override
    public void write(final JsonWriter out, final Pair value)
            throws IOException {
        out.beginObject();

        out.name("title");
        embedded.toJson(embedded.toJsonTree(value.getKey()), out);

        out.name("imageUrl");
        embedded.toJson(embedded.toJsonTree(value.getValue()), out);

        out.endObject();
    }

    @Override
    public Pair read(JsonReader in) throws IOException {
    	String left = "";
    	String right = "";
    	in.beginObject();
    	while (in.hasNext()) {
			switch (in.nextName()) {
			case "title":
				left = in.nextString();
				break;
			case "imageUrl":
				right = in.nextString();
				break;

			default:
				break;
			}
		}
    	
    	in.endObject();
        return new MutablePair<>(left, right);
    }
}

class PairSerializer implements JsonSerializer<Pair> {

    public PairSerializer() {
        super();
    }

    @Override
    public JsonElement serialize(final Pair value, final Type type,
            final JsonSerializationContext context) {
        final JsonObject jsonObj = new JsonObject();
        jsonObj.add("title", context.serialize(value.getKey()));
        jsonObj.add("imageUrl", context.serialize(value.getValue()));

        return jsonObj;
    }
}

class PairTypeAdapterFactory implements TypeAdapterFactory {

    public PairTypeAdapterFactory() {
        super();
    }

    public JsonElement serialize(final Pair value, final Type type,
            final JsonSerializationContext context) {
        final JsonObject jsonObj = new JsonObject();
        jsonObj.add("title", context.serialize(value.getKey()));
        jsonObj.add("imageUrl", context.serialize(value.getValue()));

        return jsonObj;
    }

	@Override
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (!Pair.class.isAssignableFrom(type.getRawType())) return null;
        return (TypeAdapter<T>) new PairAdapter();
	}
}

