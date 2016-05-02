package app;

/**
 * Created by shaox on 2016/3/28.
 */
public class AppConfig {
    private static String ip = "10.139.71.244";
    // Server user login url
    public static String URL_LOGIN = "http://" + ip + "/android_login_api/login.php";

    // Server user register url
    public static String URL_REGISTER = "http://" + ip + "/android_login_api/register.php";

    // Server user createquestion url
    public static String URL_CREATEQUESTION = "http://" + ip + "/android_login_api/createquestion.php";

    // Server user showquestion url
    public static String URL_SHOWQUESTION = "http://" + ip + "/android_login_api/showquestion.php";

    public static String URL_DELETEQUESTION = "http://" + ip + "/android_login_api/deletequestion.php";

    public static String URL_EDITQUESTION_M = "http://" + ip + "/android_login_api/updatequestion_m.php";

    public static String URL_EDITQUESTION_F = "http://" + ip + "/android_login_api/updatequestion_f.php";

    public static String URL_EDITQUESTION_T = "http://" + ip + "/android_login_api/updatequestion_t.php";

    public static String URL_GETFIRSTID = "http://" + ip + "/android_login_api/getid.php";

    public static String URL_UPDATEGRADEBOOK = "http://" + ip + "/android_login_api/updategradebook.php";

    public static String URL_GETMAXSETNUM = "http://" + ip + "/android_login_api/getmaxsetnum.php";

    public static String URL_SHOWGRADEBOOK = "http://" + ip + "/android_login_api/showgradebook.php";
}