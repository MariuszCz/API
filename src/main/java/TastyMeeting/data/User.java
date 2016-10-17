/*
 * Copyright 2014-2015 the original author or authors.
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

package TastyMeeting.data;

import io.swagger.annotations.ApiModel;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@ApiModel(value = "User")
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements UserDetails {
    @Id
    private String id;

    private String email;

    private String password;

    private String name;

    private String surname;

    private Set<Role> roles = new HashSet<Role>();

    private String birthdate;

    private String gender;

    private ArrayList<Interest> interests = new ArrayList<Interest>();

    private ArrayList<CulinaryPreference> culinaryPreferences = new ArrayList<>();

    private boolean active = true;

    private String facebookUID;

    private String geoLat;

    private String geoLon;

    private double distance;

    public User() {
    }

    public User(String id, String email, String password, String name, String surname, String birthdate, String gender, ArrayList interests, ArrayList culinaryPreferences, boolean active, String geoLat, String geoLon, double distance) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.birthdate = birthdate;
        this.gender = gender;
        this.interests = interests;
        this.culinaryPreferences = culinaryPreferences;
        this.active = active;
        this.geoLat = geoLat;
        this.geoLon = geoLon;
        this.distance = distance;
    }

    public User(User user) {
        super();
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.roles = user.getRoles();
        this.birthdate = user.getBirthdate();
        this.gender = user.getGender();
        this.interests = user.getInterests();
        this.culinaryPreferences = user.getCulinaryPreferences();
        this.active = user.active;
        this.geoLon =  user.getGeoLon();
        this.geoLat = user.getGeoLat();
        this.distance = user.getDistance();
    }

    public String getGeoLat() {
        return geoLat;
    }

    public void setGeoLat(String geoLat) {
        this.geoLat = geoLat;
    }

    public String getGeoLon() {
        return geoLon;
    }

    public void setGeoLon(String geoLon) {
        this.geoLon = geoLon;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //    @XmlTransient
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public ArrayList<Interest> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<Interest> interests) {
        this.interests = interests;
    }

    public ArrayList<CulinaryPreference> getCulinaryPreferences() {
        return culinaryPreferences;
    }

    public void setCulinaryPreferences(ArrayList<CulinaryPreference> culinaryPreferences) {
        this.culinaryPreferences = culinaryPreferences;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getFacebookUID() {
        return facebookUID;
    }

    public void setFacebookUID(String facebookUID) {
        this.facebookUID = facebookUID;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof User))
            return false;
        User otherUser = (User) other;

        return this.getId().equals(otherUser.getId());
    }

    @Override
    @XmlTransient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    @XmlTransient
    public String getUsername() {
        return getEmail();
    }

    @Override
    @XmlTransient
    public boolean isAccountNonExpired() {
        return isEnabled();
    }

    @Override
    @XmlTransient
    public boolean isAccountNonLocked() {
        return isEnabled();
    }

    @Override
    @XmlTransient
    public boolean isCredentialsNonExpired() {
        return isEnabled();
    }

    @Override
    @XmlTransient
    public boolean isEnabled() {
        return isActive();
    }

}
