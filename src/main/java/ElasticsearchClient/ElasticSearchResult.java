package ElasticsearchClient;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/8/29.
 * @Description :
 */

public class ElasticSearchResult {

    private double score;

    private String name;

    public ElasticSearchResult() {
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}