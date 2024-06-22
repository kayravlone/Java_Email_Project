//@Author Kayra Cansin Gokmen

package javaemailproject;


public class EmailAccount {
    private String receivePort;
    private String sendPort;
    private String toReceive;
    private String toSend;
    private String username;
    private String password;
    private String protocol;

    public EmailAccount( String receivePort, String sendPort, String toReceive, String toSend,String username, String password,String protocol) {
        this.receivePort = receivePort;
        this.sendPort = sendPort;
        this.toReceive = toReceive;
        this.toSend = toSend;
        this.username=username;
        this.password=password;
        this.protocol=protocol;
    }

    
    public String getReceivePort() {
        return receivePort;
    }

    
    public String getSendPort() {
        return sendPort;
    }

    
    public String getToReceive() {
        return toReceive;
    }

    
    public String getToSend() {
        return toSend;
    }

    
    public String getUsername() {
        return username;
    }

    
    public String getPassword() {
        return password;
    }

    
    public String getProtocol() {
        return protocol;
    }
    
    
    
    
}
