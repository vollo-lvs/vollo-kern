package nl.vollo.kern;

import org.apache.commons.math3.primes.Primes;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.apache.commons.math3.primes.Primes.isPrime;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SuppressWarnings({"Convert2MethodRef", "SameParameterValue"})
class DemoTest {

    /**
     * Calculates the sum of the squares of the first n prime numbers.
     */
    int addUp(int n) {
        int i = 0;
        int prime = 1;
        int sum = 0;
        while (i < n) {
            if (isPrime(prime)) {
                sum += prime * prime;
                i++;
            }
            prime++;
        }
        return sum;
    }

    int addUp2(int n) {
        return Stream.iterate(1, i -> i + 1)
                .filter(i -> isPrime(i))
                .mapToInt(i -> i * i)
                .limit(n)
                .sum();
    }

    @Test
    void shouldAddUp() {
        assertThat(addUp(100), is(8384727));
        assertThat(addUp2(100), is(8384727));
    }
}
