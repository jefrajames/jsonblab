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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import org.junit.AfterClass;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * These test cases illustrate some basic usages of JSON-B in particular
 * specific behaviors with null and empty Optional values
 *
 * It also allustrates how the @JsonbProperty annotation can force empty
 * Optional to be serialized as null.
 *
 * @author jefrajames
 */
public class PersonTest {

    // Jsonb class is multi-threaded
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

    // Jef has all attributes initialized
    private static Person generateJef() throws ParseException {
        java.time.LocalDate birthDate = LocalDate.of(1971, Month.MAY, 28);
        java.util.Date weddingDate = new SimpleDateFormat("dd.MM.yyy").parse("06.08.1987");
        return new Person("Jef", Gender.MALE, birthDate, Optional.of(weddingDate));
    }

    // Paul has en empty optional weddingDate
    private static Person generatePaul() {
        java.time.LocalDate birthDate = LocalDate.of(1992, Month.OCTOBER, 9);
        return new Person("Paul", Gender.MALE, birthDate, Optional.empty());
    }

    // Vinc has a null birthDate
    private static Person generateVinc() {
        java.time.LocalDate birthDate = LocalDate.of(1989, Month.OCTOBER, 23);
        return new Person("Vinc", Gender.MALE, birthDate, Optional.empty());
    }

    /**
     * Jeff has all attributes initialized, all of them are serialized and
     * deserialzed normally.
     *
     * @throws ParseException
     */
    @Test
    public void testComplete() throws ParseException {

        Person initialData = generateJef();

        String jsonForm = jsonb.toJson(initialData, Person.class);
        // jsonForm={"birthDate":"1971-05-28","gender":"MALE","name":"Jef","weddingDate":"1987-08-05T22:00:00Z[UTC]"}

        Person fromJson = jsonb.fromJson(jsonForm, Person.class);

        assertEquals(initialData, fromJson);
    }

    /**
     * Paul has an empty Optional weeddingDate. Equality before and after
     * serialization is made possible thanks to the @JsonbProperty(nillable =
     * true) annotation on weddingDate property which forces its representation
     * as null value in the JSON form. Without it, this empty Optional property
     * would be absent from the Json form. Hence the Optional property would be
     * null after deserialisation instead of empty optional.
     *
     * See chapters 3.14 and 4.3 of the JSR-367 specification
     *
     * @throws ParseException
     */
    @Test
    public void testEmptyOptional() throws ParseException {

        Person initialData = generatePaul();

        String jsonForm = jsonb.toJson(initialData, Person.class);
        // jsonForm={"birthDate":"1992-10-09","gender":"MALE","name":"Paul","weddingDate":null}

        Person fromJson = jsonb.fromJson(jsonForm, Person.class);

        assertEquals(initialData, fromJson);
    }

    /**
     * Vinc has a null birthDate. Here we can see that birthDate is not
     * represented in the JSON form. See chapter 3.14 of the spec: The result of
     * serializing a java field with a null value is the absence of the property
     * in the resulting JSON document.
     *
     * @throws ParseException
     */
    @Test
    public void testNullValue() throws ParseException {

        Person initialData = generateVinc();

        String jsonForm = jsonb.toJson(initialData, Person.class);
        // jsonForm={"birthDate":"1989-10-23","gender":"MALE","name":"Vinc","weddingDate":null}

        Person fromJson = jsonb.fromJson(jsonForm, Person.class);

        assertEquals(initialData, fromJson);
    }

    /**
     * 
     * Test all objects using arrays.
     * 
     * @throws ParseException 
     */
    @Test
    public void testArray() throws ParseException {

        Person[] initialData = {
            generateJef(),
            generatePaul(),
            generateVinc()
        };

        String jsonForm = jsonb.toJson(initialData);

        Person[] fromJson = jsonb.fromJson(jsonForm, Person[].class);

        assertTrue(fromJson.length == initialData.length);
        assertArrayEquals(initialData, fromJson);
    }

}
