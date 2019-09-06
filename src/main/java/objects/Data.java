package main.java.objects;

/**
 * Created by sroca on 05/07/2017.
 */
public class Data {

    public static class Message {
        public static class Attachment {

            private String contentType;
            private String data;
            private int size;

            public Attachment () {
            }

            public String getContentType() {
                return contentType;
            }
            public String getData() {
                return data;
            }
            public int getSize() {
                return size;
            }

            public void setContentType(String contentType) {
                this.contentType = contentType;
            }
            public void setData(String data) {
                this.data = data;
            }
            public void setSize(int size) {
                this.size = size;
            }

        }

        private long timestamp;
        private main.java.objects.Data.Message.Attachment attachments;
        private String body;

        public Message() {

        }
        public Message(String body) {
            this.body = body;
        }

        public long getTimestamp() {
            return timestamp;
        }
        public String getBody() {
            return body;
        }
        public main.java.objects.Data.Message.Attachment getAttachments() {
            return attachments;
        }

        public void setTimestamp (long timestamp) {
            this.timestamp = timestamp;
        }
        public void setBody(String body) {
            this.body = body;
        }
        public void setAttachments(main.java.objects.Data.Message.Attachment attachments) {
            this.attachments = attachments;
        }
    }

    private String user;
    private String platform;
    private Message message;

    public Data (){
        this.platform = "";
        }

    public Data(String user, Message message) {
        this.user = user;
        this.message = message;
        this.platform = "";
    }

    public String getUser() {
        return user;
    }
    public String getPlatform() {
        return platform;
    }
    public Message getMessage() {
        return message;
    }

    public void setUser(String user) {
        this.user = user;
    }
    public void setPlatform(String platform) {
        this.platform = platform;
    }
    public void setMessage(Message message) {
        this.message = message;
    }
}
