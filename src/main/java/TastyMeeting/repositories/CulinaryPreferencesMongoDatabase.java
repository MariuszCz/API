package TastyMeeting.repositories;

import TastyMeeting.Exceptions.WrongIdException;
import TastyMeeting.data.CulinaryPreference;
import TastyMeeting.repositories.interfaces.CulinaryPreferencesDatabase;
import TastyMeeting.repositories.mongo.CulinaryPreferenceRepository;

import java.util.List;

/**
 * Created by rafal on 5/23/16.
 */
public class CulinaryPreferencesMongoDatabase implements CulinaryPreferencesDatabase {
    CulinaryPreferenceRepository culinaryPreferenceRepository;

    public CulinaryPreferencesMongoDatabase(CulinaryPreferenceRepository culinaryPreferenceRepository) {
        this.culinaryPreferenceRepository = culinaryPreferenceRepository;
    }

    @Override
    public List<CulinaryPreference> getCulinaryPreferences() {
        return culinaryPreferenceRepository.findAll();
    }

    @Override
    public void addCulinaryPreference(CulinaryPreference culinaryPreference) {
        culinaryPreferenceRepository.save(culinaryPreference);
    }

    @Override
    public void updateCulinaryPreference(String id, CulinaryPreference culinaryPreference) {
        culinaryPreference.setId(id);
        culinaryPreferenceRepository.save(culinaryPreference);
    }

    @Override
    public void deleteCulinaryPreference(String id) throws WrongIdException {
        culinaryPreferenceRepository.delete(id);
    }

    @Override
    public CulinaryPreference getCulinaryPreference(String id) {
        return culinaryPreferenceRepository.findOne(id);
    }

    @Override
    public boolean checkIfPreferenceExists(String culinaryPreferenceName) {
        return culinaryPreferenceRepository.findByCulinaryPreferenceName(culinaryPreferenceName) != null;
    }
}
