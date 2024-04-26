package kr.co.seesoft.nemo.starnemoapp.nemoapi.ro;

import java.io.Serializable;


public class NemoCdNmListRO implements Serializable {

    /**  */
    public String code;
    /**  */
    public String name;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @Override
    public String toString() {
        return "NemoCdNmListRO{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

