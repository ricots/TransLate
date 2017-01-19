package by.vshkl.translate.model;

public class Stop {
    private String url;
    private String name;
    private String direction;

    public Stop() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Stop{");
        sb.append("url='").append(url).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", direction='").append(direction).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stop stop = (Stop) o;

        return url.equals(stop.url);

    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }
}
