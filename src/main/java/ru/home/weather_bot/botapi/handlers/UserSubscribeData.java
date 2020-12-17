package ru.home.weather_bot.botapi.handlers;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.Location;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSubscribeData {
    float default_lat;
    float default_lon;
    boolean has_def_loc = false;
    boolean is_subscribe = false;
    Location default_location;

    public void setDefault_location(Location location){
        this.default_location = location;
    }

    public void setDefault_location(float lat, float lon){
        this.default_lat = lat;
        this.default_lon = lon;
        this.has_def_loc = true;
    }

    public String getDefault_location() {

        if(has_def_loc) {
            String def_loc = default_lat + " " + default_lon;
            return def_loc;
        }else{
            return "null";
        }
    }

    public float getDefault_lat(){
        return default_lat;
    }

    public float getDefault_lon(){
        return default_lon;
    }

    public void setIs_subscribe(boolean is_subscribe){
        this.is_subscribe = is_subscribe;
    }

    public boolean getIs_subscribe(){ return is_subscribe; }
}
