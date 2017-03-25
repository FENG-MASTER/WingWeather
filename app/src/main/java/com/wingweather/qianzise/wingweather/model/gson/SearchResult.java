package com.wingweather.qianzise.wingweather.model.gson;

import java.util.List;

/**
 * Created by qianzise on 2017/3/25 0025.
 */

public class SearchResult {
    private List<HeWeather5Bean> HeWeather5;


    public boolean hasFound(){
        String s=getHeWeather5().get(0).getStatus();
        return s.toLowerCase().equals("ok");
    }

    public String getChineseName(){
        return getHeWeather5().get(0).getBasic().getCity();
    }


    public List<HeWeather5Bean> getHeWeather5() {
        return HeWeather5;
    }

    public void setHeWeather5(List<HeWeather5Bean> HeWeather5) {
        this.HeWeather5 = HeWeather5;
    }

    public static class HeWeather5Bean {
        private BasicBean basic;
        private String status;

        public BasicBean getBasic() {
            return basic;
        }

        public void setBasic(BasicBean basic) {
            this.basic = basic;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public static class BasicBean {


            private String city;
            private String cnty;
            private String id;
            private String lat;
            private String lon;
            private String prov;

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getCnty() {
                return cnty;
            }

            public void setCnty(String cnty) {
                this.cnty = cnty;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getLon() {
                return lon;
            }

            public void setLon(String lon) {
                this.lon = lon;
            }

            public String getProv() {
                return prov;
            }

            public void setProv(String prov) {
                this.prov = prov;
            }
        }
    }
}
