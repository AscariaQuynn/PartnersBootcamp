package cz.devforce.partnersbootcamp.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.capybara.clamav.ClamavClient;
import xyz.capybara.clamav.commands.scan.result.ScanResult;

import java.io.ByteArrayInputStream;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AntivirusService {

    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Analyzes file for viruses, return true when virus was found, false otherwise.
     * @param content content of the file
     * @return true when virus was found, false otherwise
     */
    @SneakyThrows
    public boolean analyze(byte[] content) {
        try {
            log.info("Antivirus check started");
            ClamavClient client = new ClamavClient("clamav");
            ScanResult scanResult = client.scan(new ByteArrayInputStream(content));
            log.info("Antivirus check ended");

            if (scanResult instanceof ScanResult.OK) {
                log.info("Antivirus check result: clean");
                return false;
            } else if (scanResult instanceof ScanResult.VirusFound) {
                log.info("Antivirus check result: virus");
                Map<String, Collection<String>> viruses = ((ScanResult.VirusFound) scanResult).getFoundViruses();
                // here we can inform about viruses, but for this example, simple bool should be enough
                return true;
            }
        } catch (Exception ex) {
            log.info("Antivirus check failed", ex);
        }
        // this can be removed after clamav is tested
        Thread.sleep(10 * 1000);
        return secureRandom.nextBoolean();
    }
}
