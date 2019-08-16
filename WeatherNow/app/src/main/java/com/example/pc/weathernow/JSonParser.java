package com.example.pc.weathernow;

import com.example.pc.weathernow.api.Weather;
import com.example.pc.weathernow.api.WeatherResponse;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kubas on 26.12.2017.
 */


public class JSonParser {


    //Parser JSON to weatherResponse
    public static WeatherResponse DeserializeJSon(String dataJSon)
    {
        //Set of regexes to match apropriate fields
        if(dataJSon.length() > 0)
        {
            WeatherResponse weatherResponse = new WeatherResponse();

            //Coord
            Pattern p1 = Pattern.compile("\"coord\":\\{\"lon\":(.*),\"lat\":([0-9]*\\.{0,1}[0-9]*)\\},");
            Matcher m1 = p1.matcher(dataJSon);
            if(m1.find())
            {
                weatherResponse.coord.lon = Double.parseDouble(m1.group(1));
                weatherResponse.coord.lat = Double.parseDouble(m1.group(2));
            }



            //Weather
            Pattern p2_1 = Pattern.compile("\"weather\":\\[(.*)\\],");
            Matcher m2_1 = p2_1.matcher(dataJSon);
            if(m2_1.find())
            {
                String table = m2_1.group(1);
                ArrayList<String> weathers = new ArrayList<String>();
                int index=0;
                for(char e : table.toCharArray())
                {
                    if(e == '{')
                    {
                        weathers.add("");
                    }
                    else if( e == '}')
                    {
                        index++;
                    }
                    else if( e == ',')
                        continue;
                    else
                        weathers.set(index, weathers.get(index) + e);
                }

                for(String w : weathers)
                {
                    System.out.println(w);
                    Pattern p2_2 = Pattern.compile("\"id\":(.*)\"main\":\"(.*)\"\"description\":\"(.*)\"\"icon\":\"(.*)\"");
                    Matcher m2_2 = p2_2.matcher(w);
                    if(m2_2.find())
                    {
                        Weather weather = new Weather();
                        weather.id = Long.parseLong(m2_2.group(1));
                        weather.main = m2_2.group(2);
                        weather.description = m2_2.group(3);
                        weather.icon = m2_2.group(4);

                        weatherResponse.weather.add(weather);
                    }
                    else
                        System.out.println("Table in weather not found");
                }
            }


            //Base
            Pattern p3 = Pattern.compile("\"base\":\"(\\w+)\",");
            Matcher m3 = p3.matcher(dataJSon);
            if(m3.find())
            {
                weatherResponse.base = m3.group(1);
            }


            //Main
            Pattern p4 = Pattern.compile("\"main\":\\{\"temp\":(.*),\"pressure\":(.*),\"humidity\":(.*),\"temp_min\":(.*),\"temp_max\":([0-9]*\\.{0,1}[0-9]*)\\}");            Matcher m4 = p4.matcher(dataJSon);
            if(m4.find())
            {
                weatherResponse.main.temp = Double.parseDouble(m4.group(1));
                weatherResponse.main.pressure = Double.parseDouble(m4.group(2));
                weatherResponse.main.humidity = Long.parseLong(m4.group(3));
                weatherResponse.main.tempMin = Double.parseDouble(m4.group(4));
                weatherResponse.main.tempMax = Double.parseDouble(m4.group(5));
            }


            //Visibility
            Pattern p5 = Pattern.compile("\"visibility\":([0-9]*\\.{0,1}[0-9]*)");
            Matcher m5 = p5.matcher(dataJSon);
            if(m5.find())
            {
                weatherResponse.visibility = Long.parseLong(m5.group(1));
            }


            //Wind
            Pattern p6 = Pattern.compile("\"wind\":\\{\"speed\":([0-9]*\\.{0,1}[0-9]*),\"deg\":([0-9]*\\.{0,1}[0-9]*)\\}");
            Matcher m6 = p6.matcher(dataJSon);
            if(m6.find())
            {
                weatherResponse.wind.speed = Double.parseDouble(m6.group(1));
                weatherResponse.wind.deg = Double.parseDouble(m6.group(2));
            }


            //Clouds
            Pattern p7 = Pattern.compile("\"clouds\":\\{\"all\":([0-9]*\\.{0,1}[0-9]*)\\}");
            Matcher m7 = p7.matcher(dataJSon);
            if(m7.find())
            {
                weatherResponse.clouds.all = Long.parseLong(m7.group(1));
            }


            //Rain
            Pattern p8 = Pattern.compile("\"rain\":\\{\"3h\":([0-9]*\\.{0,1}[0-9]*)\\}");
            Matcher m8 = p8.matcher(dataJSon);
            if(m8.find())
            {
                //TODO add rain to WeatherResponse
            }


            //Snow
            Pattern p9 = Pattern.compile("\"snow\":\\{\"3h\":([0-9]*\\.{0,1}[0-9]*)\\}");
            Matcher m9 = p9.matcher(dataJSon);
            if(m9.find())
            {
                //TODO add snow to weather response
            }


            //
            Pattern p10 = Pattern.compile("\"dt\":([0-9]*\\.{0,1}[0-9]*)");
            Matcher m10 = p10.matcher(dataJSon);
            if(m10.find())
            {
                weatherResponse.dt = Long.parseLong(m10.group(1));
            }


            //sys
            Pattern p11 = Pattern.compile("\"sys\":\\{\"type\":(.*),\"id\":(.*),\"message\":(.*),\"country\":\"(.*)\",\"sunrise\":(.*),\"sunset\":([0-9]*\\.{0,1}[0-9]*)\\}");
            Matcher m11 = p11.matcher(dataJSon);
            if(m11.find())
            {
                weatherResponse.sys.type = Long.parseLong(m11.group(1));
                weatherResponse.sys.id = Long.parseLong(m11.group(2));
                weatherResponse.sys.message = Double.parseDouble(m11.group(3));
                weatherResponse.sys.country= m11.group(4);
                weatherResponse.sys.sunrise = Long.parseLong(m11.group(5));
                weatherResponse.sys.sunset = Long.parseLong(m11.group(6));
            }


            //ID, name, cod
            Pattern p12 = Pattern.compile("\"id\":([0-9]*\\.{0,1}[0-9]*),\"name\":\"(.*)\",\"cod\":([0-9]*\\.{0,1}[0-9]*)");
            Matcher m12  = p12.matcher(dataJSon);
            if(m12.find())
            {
                weatherResponse.id = Long.parseLong(m12.group(1));
                weatherResponse.name = m12.group(2);
                weatherResponse.cod = Long.parseLong(m12.group(3));
            }

            return weatherResponse;
        }
        else
            return null;
    }
}
