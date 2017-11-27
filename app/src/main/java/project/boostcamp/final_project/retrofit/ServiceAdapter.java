package project.boostcamp.final_project.retrofit;


public class ServiceAdapter {

    public static final String BaseUrl = "https://openapi.naver.com/v1/search/";

    public static NaverService getService() {
        return RestClient.getClient(BaseUrl).create(NaverService.class);
    }

}
