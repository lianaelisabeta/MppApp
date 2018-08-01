package services;

public class ValidationExc extends RuntimeException {
    String message;
    public ValidationExc(String msg){
        message=msg;
    }
    public String getMessage(){
        return message;
    }
}
