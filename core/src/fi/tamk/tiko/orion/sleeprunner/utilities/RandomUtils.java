package fi.tamk.tiko.orion.sleeprunner.utilities;


import java.util.Random;

import fi.tamk.tiko.orion.sleeprunner.data.EnemyType;

/**
 * Random number generating methods.
 */
public class RandomUtils {

    /**
     * Uses RandomEnum-method to choose a random enum from EnemyType-enumlist.
     * @return randomed enum
     */
    public static EnemyType getRandomEnemyType(){
        RandomEnum<EnemyType> randomEnum = new RandomEnum<EnemyType>(EnemyType.class);
        return randomEnum.random();
    }

    /**
     * Picks a random enum from the received enumlist.
     * http://stackoverflow.com/a/1973018
     * @param <E>
     */

    private static class RandomEnum<E extends Enum> {

        private static final Random RND = new Random();
        private final E[] values;

        public RandomEnum(Class<E> token) {
            values = token.getEnumConstants();
        }

        public E random() {
            return values[RND.nextInt(values.length)];
        }
    }

}
