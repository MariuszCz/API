package TastyMeeting.repositories;

import TastyMeeting.Exceptions.WrongIdException;
import TastyMeeting.data.Interest;
import TastyMeeting.repositories.interfaces.InterestsDatabase;
import TastyMeeting.repositories.mongo.InterestRepository;

import java.util.List;

/**
 * Created by rafal on 5/23/16.
 */
public class InterestsMongoDatabase implements InterestsDatabase {
    InterestRepository interestRepository;

    public InterestsMongoDatabase(InterestRepository interestRepository) {
        this.interestRepository = interestRepository;
    }

    @Override
    public List<Interest> getInterests() {
        return interestRepository.findAll();
    }

    @Override
    public void addInterest(Interest interest) {
        interestRepository.save(interest);
    }

    @Override
    public void updateInterest(String id, Interest interest) {
        interest.setId(id);
        interestRepository.save(interest);
    }

    @Override
    public void deleteInterest(String id) throws WrongIdException {
        interestRepository.delete(id);
    }

    @Override
    public Interest getInterest(String id) {
        return interestRepository.findOne(id);
    }

    @Override
    public boolean checkIfInterestExists(String interestName) {
        return interestRepository.findByInterestName(interestName) != null;
    }
}
