package cs.Enum;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/25.
 * @Description :
 */
public enum BasicOperation implements Operation {

    PLUS("+") {
        public double apply(double x, double y) {
            return x + y;
        }
    },
    MIUS("-") {
        public double apply(double x, double y) {
            return x - y;
        }
    },
    TIMES ("*") {
        public double apply(double x, double y) {
            return x * y;
        }
    },
    DEVIDE ("/") {
        public double apply(double x, double y) {
            return x / y;
        }
    };
    private final String symbol;

    BasicOperation(String symbol) {
        this.symbol = symbol;
    }

}
