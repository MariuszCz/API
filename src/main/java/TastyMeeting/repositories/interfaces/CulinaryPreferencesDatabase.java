package TastyMeeting.repositories.interfaces;

import TastyMeeting.Exceptions.WrongIdException;
import TastyMeeting.data.CulinaryPreference;

import java.util.List;

/**
 * Created by mariusz on 03.05.16.
 */
public interface CulinaryPreferencesDatabase {
    List<CulinaryPreference> getCulinaryPreferences();

    void addCulinaryPreference(CulinaryPreference culinaryPreference);

    void updateCulinaryPreference(String id, CulinaryPreference culinaryPreference);

    void deleteCulinaryPreference(String id) throws WrongIdException;

    CulinaryPreference getCulinaryPreference(String id);

    boolean checkIfPreferenceExists(String culinaryPreferenceName);
}
