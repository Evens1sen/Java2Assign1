import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Predicate;

public class Practice3 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String funcInfo = """
                Please input the function No:
                1 - get even numbers
                2 - Get odd numbers
                3 - Get prime numbers
                4 - Get prime numbers that are bigger than 5
                0 - Quit""";

        while (true) {
            System.out.println(funcInfo);

            int funcNum = in.nextInt();
            if (funcNum == 0) {
                break;
            }

            System.out.println("Input size of the list:");
            int size = in.nextInt();

            System.out.println("Input elements of the list:");
            int[] nums = new int[size];
            for (int i = 0; i < size; i++) {
                nums[i] = in.nextInt();
            }

            int[] res = filter(nums, funcNum);
            System.out.println("Filter results:");
            System.out.println(Arrays.toString(res));
        }

    }

    public static int[] filter(int[] nums, int funcNum) {
        if (funcNum == 1) {
            return Arrays.stream(nums).filter((i) -> i % 2 == 0).toArray();
        } else if (funcNum == 2) {
            return Arrays.stream(nums).filter((i) -> i % 2 != 0).toArray();
        } else if (funcNum == 3) {
            return Arrays.stream(nums).filter(Practice3::isPrime).toArray();
        } else if (funcNum == 4) {
            return Arrays.stream(nums).filter(Practice3::isPrime).filter((i) -> i > 5).toArray();
        }

        return nums;
    }

    public static boolean isPrime(int n) {
        if (n == 1) {
            return false;
        }

        for (int i = 2; i < n; i++) {
            if (n % i == 0) {
                return false;
            }
        }

        return true;
    }

}
