package tmcit.hokekyo1210.SolverUI.Util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import tmcit.hokekyo1210.SolverUI.Main;


public class HttpUtil {


	private String teamToken;
	private String problemID;
	private String answer;

	public HttpUtil(String teamToken,String problemID,String answer){
		this.teamToken = teamToken;
		this.problemID = problemID;
		this.answer = answer;
	}

	public void sendAnswer() throws Exception{
		Content content = Request.Post(Main.TARGET_HOST_POST).addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
						  .bodyForm(Form.form().add("playerid", teamToken).add("problemid", problemID).add("answer",answer).build())
						  .execute().returnContent();
		System.out.println(content.asString());
	}

	public static Path getProblemFile(String problemName) throws Exception{
		File dir = new File(Main.tmpDir);
		if(!dir.exists()){
			dir.mkdirs();
		}
		Content content = Request.Get(Main.TARGET_HOST_GET+"/"+problemName)
						  .execute().returnContent();
		Path tmpPath = Paths.get(Main.tmpDir, problemName);
		Files.copy(content.asStream(), tmpPath, StandardCopyOption.REPLACE_EXISTING);
		return tmpPath;
	}










    /*public static void sendAnswer(String teamToken,String problemID,String answer) throws Exception {

    DefaultHttpClient httpclient = null;
    HttpPost post = null;
    HttpEntity entity = null;

        try {
        	httpclient = new DefaultHttpClient();

			HttpParams httpParams = httpclient.getParams();
			//接続確立のタイムアウトを設定（単位：ms）
			HttpConnectionParams.setConnectionTimeout(httpParams, 500*1000);
			//接続後のタイムアウトを設定（単位：ms）
			HttpConnectionParams.setSoTimeout(httpParams, 500*1000);

			post = new HttpPost(TARGET_HOST);
	    	post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

	    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("playerid", teamToken));
	    	params.add(new BasicNameValuePair("problemid", problemID));
	    	params.add(new BasicNameValuePair("answer", answer));
	    	post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

	    	final HttpResponse response = httpclient.execute(post);

            // レスポンスヘッダーの取得(ファイルが無かった場合などは404)
            System.out.println("StatusCode=" + response.getStatusLine().getStatusCode());

            if(response.getStatusLine().getStatusCode() != 200 ){

            	System.out.println("StatusCode:" + response.getStatusLine().getStatusCode());
                return;
            }

            entity = response.getEntity();

            // entityが取れなかった場合は、Connectionのことは心配しないでもOK
            if (entity != null) {

                System.out.println(EntityUtils.toString(entity));

                System.out.println("length: " + entity.getContentLength());

                EntityUtils.consume(entity);
                //depriciated
                entity.consumeContent();
                post.abort();
            }

            System.out.println("結果を取得しました。");

        }catch (Exception e) {
            e.printStackTrace();

        }finally {
            httpclient.getConnectionManager().shutdown();

        }
    }*/

}
