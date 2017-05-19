/*
 * Copyright 2017 jefrajames.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lab.jefrajames.jsonb;

import java.lang.reflect.Constructor;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.annotation.JsonbProperty;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This test illustrates how to use JSOB with a nested class.
 *
 *
 * Reminder on terminology Nested classes are divided into two categories:
 * static and non-static. Nested classes that are declared static are called
 * static nested classes. Non-static nested classes are called inner classes.
 *
 * According to the spec (see chapters 3.7.2 and 3.7.3) binding of nested
 * classes should work.
 *
 * Warnings: 1/ The nested class must be explictely declared as public or
 * protected 2) The nested class should be declared static. If not, the
 * reference to the outer class is implicetly added as constructor parameter
 * which prevents from having a mere noargs constructor. Hence, at runtime,
 * Yasson cannot find a default constructor: Can't create instance of a class:
 * class lab.jefrajames.jsonb.NestedClassTest$Character, No default constructor
 * found.
 *
 * Java behavior regarding inner-class is very well explained in this article :
 * http://thecodersbreakfast.net/index.php?post/2011/09/26/Inner-classes-and-the-myth-of-the-default-constructor
 *
 * A topic has been opened on the Yasson User List:
 * https://www.eclipse.org/forums/index.php/t/1086296/
 *
 * @author jefrajames
 */
public class NestedClassTest {

    private static Jsonb jsonb;

    @BeforeClass
    public static void beforeClass() {
        jsonb = JsonbBuilder.create();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        if (jsonb != null) {
            jsonb.close();
        }
    }

    /**
     * 
     * This method has been added to check the presence of the outer class reference
     * as constructor parameter with non-static nested class.
     * 
     */
    @Test
    public void testConstructors() {
        Constructor<?>[] constructors = Character.class.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            System.out.println("constructor of nested Character class " + Arrays.toString(constructor.getParameterTypes()));
        }

    }

    @Test
    public void testWithLocalDate() throws ParseException {

        Character initialData = new Character("John", Optional.of(LocalDate.of(1971, Month.MAY, 28)));

        String jsonForm = jsonb.toJson(initialData, Character.class);

        Character fromJson = jsonb.fromJson(jsonForm, Character.class);

        assertEquals(initialData, fromJson);
    }

    @Test
    public void testWithEmptyOptional() throws ParseException {

        Character initialData = new Character("Jim", Optional.empty());

        String jsonForm = jsonb.toJson(initialData, Character.class);

        Character fromJson = jsonb.fromJson(jsonForm, Character.class);

        assertEquals(initialData, fromJson);
    }

    /**
     * Reminder: this nested class should be static and explictely declared protected or
     * public.
     *
     * If not explictely declared as public or protected : JsonbException: Error
     * getting value on: Person{name=Jim, weddingDate=Optional.empty}
     *
     * If not delared as static : Can't create instance of a class: class
     * lab.jefrajames.jsonb.NestedClassTest$Character, No default constructor
     * found.
     *
     */
    public static class Character {

        private String name;

        @JsonbProperty(nillable = true)
        private Optional<LocalDate> weddingDate;

        public Character() {
            super();
        }

        public Character(String name, Optional<LocalDate> weddingDate) {
            this.name = name;
            this.weddingDate = weddingDate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Optional<LocalDate> getWeddingDate() {
            return weddingDate;
        }

        public void setWeddingDate(Optional<LocalDate> weddingDate) {
            this.weddingDate = weddingDate;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + Objects.hashCode(this.name);
            hash = 37 * hash + Objects.hashCode(this.weddingDate);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Character other = (Character) obj;
            if (!Objects.equals(this.name, other.name)) {
                return false;
            }
            if (!Objects.equals(this.weddingDate, other.weddingDate)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "Person{" + "name=" + name + ", weddingDate=" + weddingDate + '}';
        }

    }

}
