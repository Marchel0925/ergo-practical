package dev.ergo.practical.service.log;

import dev.ergo.practical.model.Log;
import dev.ergo.practical.repository.LogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    @Override
    public Log save(Log log) {
        return logRepository.save(log);
    }
}
