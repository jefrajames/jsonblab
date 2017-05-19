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

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import javax.json.bind.annotation.JsonbProperty;

/**
 * A supposed business entity class.
 *
 * @author jefrajames
 */
public class Person {

    private static final Logger LOG = Logger.getLogger(Person.class.getName());

    private String name;
    private Gender gender;
    // Warning: 2 kinds of Date
    private java.time.LocalDate birthDate;
    
    // This property enables to force the weddingDate property to be forced as null in the JSON form
    @JsonbProperty(nillable = true)
    private Optional<java.util.Date> weddingDate;


    // Empty constructor required by JSON-B?
    public Person() {
    }

    public Person(String name, Gender gender, LocalDate birthDate, Optional<java.util.Date> weddingDate) {
        this.name = name;
        this.birthDate = birthDate;
        this.weddingDate = weddingDate;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Optional<java.util.Date> getWeddingDate() {
        return weddingDate;
    }

    public void setWeddingDate(Optional<java.util.Date> weddingDate) {
        // LOG.log(Level.INFO, "Person => calling setWeddingDate with value:{0}", weddingDate);
        this.weddingDate = weddingDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.name);
        hash = 97 * hash + Objects.hashCode(this.gender);
        hash = 97 * hash + Objects.hashCode(this.birthDate);
        hash = 97 * hash + Objects.hashCode(this.weddingDate);
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
        final Person other = (Person) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (this.gender != other.gender) {
            return false;
        }
        if (!Objects.equals(this.birthDate, other.birthDate)) {
            return false;
        }
        if (!Objects.equals(this.weddingDate, other.weddingDate)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Person{" + "name=" + name + ", gender=" + gender + ", birthDate=" + birthDate + ", weddingDate=" + weddingDate + '}';
    }

}
