package com.example.zb.retrofit2demo.bean;

/**
 * Created by zb on 2019/4/19.
 */

public class WeatherResult extends BaseResult {
    private String errCode;
    private String errMsg;
    private Result result;
    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }
    public String getErrCode() {
        return errCode;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
    public String getErrMsg() {
        return errMsg;
    }

    public void setResult(Result result) {
        this.result = result;
    }
    public Result getResult() {
        return result;
    }

    public class Result {

        private String UserCode;
        private String Name;
        private String Password;
        private String OrganCode;
        public void setUserCode(String UserCode){
            this.UserCode = UserCode;
        }
        public String getUserCode() {
            return UserCode;
        }

        public void setName(String Name) {
            this.Name = Name;
        }
        public String getName() {
            return Name;
        }

        public void setPassword(String Password) {
            this.Password = Password;
        }
        public String getPassword() {
            return Password;
        }

        public void setOrganCode(String OrganCode) {
            this.OrganCode = OrganCode;
        }
        public String getOrganCode() {
            return OrganCode;
        }

    }

}
