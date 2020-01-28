package uk.gov.ons.ctp.integration.contcencucumber.cucSteps.cases;

import static org.junit.Assert.*;

import com.godaddy.logging.Logger;
import com.godaddy.logging.LoggerFactory;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.CaseDTO;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.FulfilmentDTO;
import uk.gov.ons.ctp.integration.contcencucumber.cucSteps.TestEndpoints;
import uk.gov.ons.ctp.integration.eqlaunch.crypto.Codec;
import uk.gov.ons.ctp.integration.eqlaunch.crypto.EQJOSEProvider;
import uk.gov.ons.ctp.integration.eqlaunch.crypto.JWEHelper;
import uk.gov.ons.ctp.integration.eqlaunch.crypto.KeyStore;

public class TestCaseEndpoints extends TestEndpoints {

  private String caseId;
  private String uprn;
  private CaseDTO caseDTO;
  private List<CaseDTO> caseDTOList;
  private Exception exception;
  private static final Logger log = LoggerFactory.getLogger(TestCaseEndpoints.class);
  private String ccSmokeTestUrl;
  private String mockCaseSvcSmokeTestUrl;
  private String telephoneEndpointUrl;
  private String telephoneEndpointBody;

  private static final String SIGNING_PUBLIC_SHA1 = "f008897e548d5c4fb9271e5a85f6fc4759301f26";

  private static final String ENCRYPTION_PRIVATE_SHA1 = "bc26b780aa46f053291ba122062e6075656c2345";

  private static final String SIGNING_PUBLIC_VALUE =
      "-----BEGIN PUBLIC KEY-----\n"
          + "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAzz3vj5w4oMB9tUd+NYtc\n"
          + "DxDgvd8NsnBIZL6qmBi9ph6HzNXORe+QErby3oLDnqPImZ38XVwLY3oZ9YGhVewD\n"
          + "+p1LFT2nNMhR9mJmGz2DxOZFeAGnv2aDMOV22eF3oX6z5/VseIE1+C/CDgRGXXTr\n"
          + "wkuFkHJIe9/9F90a8em7dzbOU+v9PCnY2Xrp2h7cYEinVP8GvvJhalZpAKkqWPBi\n"
          + "sTNhw4Xoq5ZzEJ8XDDyZntysDE6m2a6fyAJ6T8tR6MJa+2FIOH32tA3CDUBqaQaQ\n"
          + "opcCYl1V5t8ecQjYCk3/ikDqgVvVXntcUDkNrDiPLJKJ5Qp3UhBZSs0hepM0+PMe\n"
          + "gN1cIFMI5MvvzX+/zP5HEb2muCsvt3xxN1vJRk28/V+FznBYAuYpurd0QggT8xyr\n"
          + "YEXBg03LArpzZYPySYuN+R7p3tbA+q5AW1BRGFwa8p9Ch7y9B5BeFwxTolz7tmDa\n"
          + "3sIRnM0quyk2/Wz7OAqseoWak1MagrXuemKZEXqmzN0k0yFNqjputB8i1lRjZ3UO\n"
          + "py51VP+UeRIOMY8lUYMasYibujTZHZSeuDyHAlDpPMg6XKnp/f/+y6qBPDOSgE0d\n"
          + "mbxy1j2BFsEeM4WRcEx+ghkqL1XXLSZFWfUSwW/WZpnFstL1GCGMjoMzgKGF+N2Q\n"
          + "Bn+otbGCZbu41Os7YUYIdn0CAwEAAQ==\n"
          + "-----END PUBLIC KEY-----";

  private static final String SIGNING_PRIVATE_VALUE =
      "-----BEGIN RSA PRIVATE KEY-----\n"
          + "MIIJKQIBAAKCAgEAzz3vj5w4oMB9tUd+NYtcDxDgvd8NsnBIZL6qmBi9ph6HzNXO\n"
          + "Re+QErby3oLDnqPImZ38XVwLY3oZ9YGhVewD+p1LFT2nNMhR9mJmGz2DxOZFeAGn\n"
          + "v2aDMOV22eF3oX6z5/VseIE1+C/CDgRGXXTrwkuFkHJIe9/9F90a8em7dzbOU+v9\n"
          + "PCnY2Xrp2h7cYEinVP8GvvJhalZpAKkqWPBisTNhw4Xoq5ZzEJ8XDDyZntysDE6m\n"
          + "2a6fyAJ6T8tR6MJa+2FIOH32tA3CDUBqaQaQopcCYl1V5t8ecQjYCk3/ikDqgVvV\n"
          + "XntcUDkNrDiPLJKJ5Qp3UhBZSs0hepM0+PMegN1cIFMI5MvvzX+/zP5HEb2muCsv\n"
          + "t3xxN1vJRk28/V+FznBYAuYpurd0QggT8xyrYEXBg03LArpzZYPySYuN+R7p3tbA\n"
          + "+q5AW1BRGFwa8p9Ch7y9B5BeFwxTolz7tmDa3sIRnM0quyk2/Wz7OAqseoWak1Ma\n"
          + "grXuemKZEXqmzN0k0yFNqjputB8i1lRjZ3UOpy51VP+UeRIOMY8lUYMasYibujTZ\n"
          + "HZSeuDyHAlDpPMg6XKnp/f/+y6qBPDOSgE0dmbxy1j2BFsEeM4WRcEx+ghkqL1XX\n"
          + "LSZFWfUSwW/WZpnFstL1GCGMjoMzgKGF+N2QBn+otbGCZbu41Os7YUYIdn0CAwEA\n"
          + "AQKCAgEAhlspDHnDXLRuyy/mauBGdp4ClhYd0yloRag3ARRJH4F7mRij+kMtrHRf\n"
          + "UFKGcDrOuojqK7yYxY1LdxbrecDhc4C2RLcLx/R27r0sZUykOOrw7rRkBHp5YyHg\n"
          + "w7Cg1lpGWIOMJzPdwWF09ZFf7Qb4Maa0mMj+pRC6DNaTuXJGzysA6Pd93Ztjsts0\n"
          + "8OxBA0sW8MvFm9WXwlDzEjKZ+b8evLMLFq+iAFwxjP0W/B/tmEiIYhI7qbTEce7p\n"
          + "TQILwFMAmSigob9IScMBo4W3dw+ChWZRbWQFZQARxEZviX63xPIBFoxq8C4Z0wiq\n"
          + "DJU+fS8jpxH5+YIP4abLpaP6G8vCYa4RltmGHa9cXzQeu7L7YJocVWwlGO5lePyM\n"
          + "KKAMXJqA/Gjy6X2DOOzec7viSZVgNqz+bne5/+FYupq7JZqo7wROuVwsS5vbUx8K\n"
          + "B8vjHmYkjWsSHk6ZA2RgAKq20rn0J2KdY5Bb93kLqoix7aeXyLjcoXGHBsGiP7B2\n"
          + "1oa4Vnxm6W9ABHUxNizEW/vGH9weydJ0Gwf03FWkNm1+tvLj3IshnpB/bTHU/ydX\n"
          + "NS6nuj1GjpFiXI+EZczVlocFzP7GAXoHRpJBgP0Ukf9J8IWFl4qOfQjhxSR8aEzV\n"
          + "tT1qwkjUJXO60BdJPTLAl8HFZVH5zkmuc2HD/IF/sFKFjMB9PwECggEBAPKZpyXc\n"
          + "CNEGJuKOd6tkwPDDct7lA5pOtd0a0NFm/oVUraMPkt9GfX04bClsRyC6yZhbJyMw\n"
          + "A/N25aOXG/Baz4P8Apek9d5m1GNdyiKZnAfhYbeJF3lNoVO8kQcKy/U60z8j38wW\n"
          + "u9id/AUBm51OAf54ae6PEHDNcWRNXQt/LoiUmP2c6akVfEkXiPFXqe2bwWureG9O\n"
          + "bsIurVWxolJMVzp0Go6YIhLXQwd9Sett6WliZ99pkwFF+4FEkUh3ru4e0tOkvCKO\n"
          + "yuTA7lDpy+zW98emwLJUy8n3Sii6d0ErRejrAx1RQW6zbUat/LIQXqBmTICYRQvH\n"
          + "M6zWJOFNm5lrEikCggEBANqwUd+KU9M67YQXi+C/ZVl2e2Cqf1KAvoGXPh/nJsUw\n"
          + "v8KJvIVK7kW1o0q8RO49WYapubBpNICPTGyWwOyfH+KSGRuloum9k331bvdaz6qw\n"
          + "zgGK1mTqFEXNElTiOsSpJaTuLt7bjdFHFsgQCOugBf8TWY6371mPgXtKvzKrFO0v\n"
          + "pMXhgpjVdlFgnXMJQpnSZdNh58Um0+zc/EjxIIDmjbHMj2SExKVKapOer1jrcBGS\n"
          + "O7JAIQrEQM0mqtenQPqe80FAPYY+SoUJjJ4YxeCOu+rOoSh1ClGL7I4g2FvhvgAC\n"
          + "2U737YidhnwERWdCFIfhkRmkoVR3e3wV6k0u5bEulDUCggEAAjW+HQ0tE2Jf9k5f\n"
          + "7rLDQy40nK0vZDd1VqdI4a9zgBluX37j0p7cw8hAy/vNhhHNhlLGP37PemdJ3jyh\n"
          + "J4ZcP5KLH4CEMNt08dbH4ZrOng/CiR55lURMxOuB0rOZegloJToZbs2CNo3x3sXN\n"
          + "+hfc0smcBW1ONAjbEJPX1iP5c4sO/bhxNHYapLvPJouq45w4ndd5CGKJhcFRGOe+\n"
          + "V8uUO9cU6tmd7dgCJ05P3xIoPyqDUbiveyJ9EQdj32ofsNGdEAp/ID12wbC0Ow52\n"
          + "KhknNq1hMf6twJA9H7PbJD/VqjKB28GCvBRsWWl6VNDrW8Cyz7UTY/ETmm509Yx0\n"
          + "b2hXSQKCAQEAtFrP2vz47u7dbaABs0QF8Mc/L1TNlpwpATVbffIjzmLK80Sm3oMS\n"
          + "iRko53znmFeuWtnlE3FgZFpKHBAkYcFGCZSV8nAjMIQxfKMKdiNFuy7/ZtQ6xpUq\n"
          + "TPq4kJrW/tPFAQWSUCdgCWWIi0x2HuUlrN0ncgWN9x3cGnNlxgLESmyNhsjZ7PO3\n"
          + "FZwJnhLYA4Y6hh9rhvPjuafyxLFgLg52c1kSNUMt7me2B7LKSBo7nbItW296EKgU\n"
          + "DV1DboE4nLi0Q28YjnsW+CsM9mHV58GvhxIMZRJJhUFRwVGjPfupt9aho3fjRVUs\n"
          + "0WCwYF1mEz5bbXuRtdioVYi5aBgyRHL2tQKCAQBd8UvLfBdQV68oKY7wtSQqm22j\n"
          + "QDooKTbP5UEyEqNZnOTJRAZck2cx0Jhpbav4mr8AYG6Z28NRT/ugcBrwIrYfALCw\n"
          + "5wQWRr5Ss+mvmL37Htn3H1zK8ynk07KWFlFPkP5a4MZ2WBiiseRkeATpY/CULtU8\n"
          + "fF4ZHF2XzRjjB7D7UNFS7vfyQuM+badal4/R/RaizuozmJMKSQyAt3RFnIek6oOk\n"
          + "Rc0Tn5hPi9ccZLbHgXp21A2lgn8pzfNzIO/Hg56I8Td+Q2E0+MJJL7Mhcccv4CdL\n"
          + "AuSIJ3MHTJ3yPMNg1yeSkau2vXPQgcrApjCuLK5Uo/mhM1bJjKOxB7FisXEC\n"
          + "-----END RSA PRIVATE KEY-----";

  private static final String ENCRYPTION_PUBLIC_VALUE =
      "-----BEGIN PUBLIC KEY-----\n"
          + "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAn2YIoRD0Bcwz6DdF3IaA\n"
          + "jCcRYv7oAFJng/af1gTpvd6iKRnkc8sHNKs0QoFrIGK7YPVqvkYCKSss46JNqSGh\n"
          + "l+eKJLiM2F1Z5zdF7wnhRMvtmYpWuWpUT+/0sdh1m/dd9XjVRyCGppcimi1NqxKB\n"
          + "EfOW7a+y11LrlLxuQsEaa5aUzB1qhq17MYzc7/cERPWpFO0hn8VuymbwJ7Tp5IvQ\n"
          + "lb0MF7WggckoEYQCDM7yh3uFFO63F37TUHvDNQ2HepcgbAqCNmsReVEB4i56GxO0\n"
          + "oO2wJpv0ch+9g0hpViB43C8IYR+DuBiduKqs6XD5Jy9QNBaC5s6pmS4mtqZEc3Kt\n"
          + "Z8N30DWUspTod4Pwhuz3i2aI+PBz5S9nmAF/yjmmubElvNCeISY/LAYk+DV9oG1M\n"
          + "CsXrzJbHyy+vWdARmFd2pfqolZkCfaLmad0gN8/1Sw7jW8xWwuV1TqJOvcphthfp\n"
          + "t0z8MzxR5aok+JZ6jxe0LVn6XDo1CNw7dehKiy4UyRDI6WM3t+FdvjnJMJu9JZJg\n"
          + "CKnRHUIUyXb7O7M6pVDnMk4ptdgm8rxpxkGl24//vKiEpuSL+VqtpPkRuv0Lk1/R\n"
          + "4AUYYeuu20rCwnXqqyHDUL4u66GokHmmlEDHEB0Z/hEX7ZvFt1PhODyXOlho1H0a\n"
          + "mZrnkh13fkWKTPVDjY/n+3ECAwEAAQ==\n"
          + "-----END PUBLIC KEY-----";

  private static final String ENCRYPTION_PRIVATE_VALUE =
      "-----BEGIN RSA PRIVATE KEY-----\n"
          + "MIIJKgIBAAKCAgEAn2YIoRD0Bcwz6DdF3IaAjCcRYv7oAFJng/af1gTpvd6iKRnk\n"
          + "c8sHNKs0QoFrIGK7YPVqvkYCKSss46JNqSGhl+eKJLiM2F1Z5zdF7wnhRMvtmYpW\n"
          + "uWpUT+/0sdh1m/dd9XjVRyCGppcimi1NqxKBEfOW7a+y11LrlLxuQsEaa5aUzB1q\n"
          + "hq17MYzc7/cERPWpFO0hn8VuymbwJ7Tp5IvQlb0MF7WggckoEYQCDM7yh3uFFO63\n"
          + "F37TUHvDNQ2HepcgbAqCNmsReVEB4i56GxO0oO2wJpv0ch+9g0hpViB43C8IYR+D\n"
          + "uBiduKqs6XD5Jy9QNBaC5s6pmS4mtqZEc3KtZ8N30DWUspTod4Pwhuz3i2aI+PBz\n"
          + "5S9nmAF/yjmmubElvNCeISY/LAYk+DV9oG1MCsXrzJbHyy+vWdARmFd2pfqolZkC\n"
          + "faLmad0gN8/1Sw7jW8xWwuV1TqJOvcphthfpt0z8MzxR5aok+JZ6jxe0LVn6XDo1\n"
          + "CNw7dehKiy4UyRDI6WM3t+FdvjnJMJu9JZJgCKnRHUIUyXb7O7M6pVDnMk4ptdgm\n"
          + "8rxpxkGl24//vKiEpuSL+VqtpPkRuv0Lk1/R4AUYYeuu20rCwnXqqyHDUL4u66Go\n"
          + "kHmmlEDHEB0Z/hEX7ZvFt1PhODyXOlho1H0amZrnkh13fkWKTPVDjY/n+3ECAwEA\n"
          + "AQKCAgAX/vpJlQ3HWahuyvNfcXgkoTUC3DD862rd+OCzDWZKyRtMaLN1oxjgmu1x\n"
          + "HZ6M75AZ1phNMKjenbtSQXrDfWagQaEQSiAZ6mPAZRfIFoqtGq4YMTVBEHrE1fDW\n"
          + "XSnHYwPoElq5LHJY3eO9phhNKqn3k94ixhJ8S+VfLMbEkeFyVObtm3gP8knbecNA\n"
          + "9MTVxM3BcrlfHg/BtKFuuNOFLm8mO6F4aEVThH7dtCvC1dy4/KVaM12o0dTqBteq\n"
          + "QvEkSV/+I87adl/ZDZLlA+4p6VuF5oJgLZYtIA4dl2cUKitHqmTuihoAOuRzim4h\n"
          + "bnpzpbHDRahLKs1pXAND99/SKW9lNKmlkc3NkMXUHSVy13ovNnuh3AbkDcMyNfaK\n"
          + "hAZMnZZ0TFXXTf9WzK2023Gvpk4YHwCxEP/800UK7lfuUAvyJiGElzN3KLX6ZeTK\n"
          + "PRYu1fZjz2uLBHr9sc8MoT3RF1PEGIPbuNAHbdaclSJuxicaek2UiN+SEo2JeywH\n"
          + "oMLJpNPYQmnXFJUUn0QxF09Q0dNQIbYNImEn6tAkZubtCU0sq05pjCfUmjo7dkfF\n"
          + "pgXJes2Ny57RWEh8bsTLyaAI6NGe31t2POfRJRe14M7FrTif6PjH6CKqy78G7L0R\n"
          + "Iq6/J+U+qXUPOZ58rwWNyX4bxrPVUlbj2h7v59acfJXg63iG6QKCAQEA0/37ci7V\n"
          + "tZFcqLbgrNMIMhj9fLTuOU/gJwh7DDi0SlerOsFXyNqHGLoEtz2ZowmLACE7UUHj\n"
          + "KEcYiUbe2orVU0efR67CBmfZpv/jbGXx4f3PC8whA+6y+MkiZEtCtB54ghqnq5k2\n"
          + "mKaQGbhst4HHWZkYRlJscBACqibIWcyOfejycQr3mKk5oRXsXX37tvdN4M+ITxyK\n"
          + "1iP7pA18SJMjnFXIbjkGM4bXKsFCUyA7n3ga/QtrnfDIqIaoSekabtek85PWkv9J\n"
          + "IPBESa0IY4NeNMhIKFg/ockOTUz3vExB9Qlx8fNyUPHp1/5q2606mvx1kBmU0YxN\n"
          + "+ZpuqymCt9IRPwKCAQEAwH0K54iLltustYIzEhW0VRwO+UDbVgPKgXIowpue2/T+\n"
          + "QoPLmbsU0D1LlOoPMoY2KA9hBFCqjVpKN+1s8V4iss3sfaTwl4jp49dcfN+8f9h6\n"
          + "YtXRQscOvtTkTeSSFQveuhFwQDz1fmvRlQTfx14702/g0hk7eo4M8EHkOrZ++1qD\n"
          + "a445SwlIpfbgg1xgk7z0/DjhYT1p26MDDhqinhfOvz2dvVdoWwqJxrwEqqV/dpLI\n"
          + "zI1zWIg4WDEftu15Lbu0bSgGfBO6Toq/jC2w9nHXZ+kUr5TYb2eML0K54hQBX4/l\n"
          + "Td607qayWzKbg0Y7kXt//xIfhXNB+Qvvb4+L33gXTwKCAQEAsO877bpPXoLbrmks\n"
          + "rrJYFdAryekNJ8KWGJ4IbVPqjl4LqTdz+E0EEsw3YWVFlQFDBbG224Q9fwGP5gvD\n"
          + "tu3HTQHrXB3tDQGidLMAcxnHP9Lh+zu9lpWkxObDEKiMHN+NvzFWHm/7VH84qG74\n"
          + "NlVYOF0CGLd0kDecsb063VBQ4aAKsfTwYHdlhSBSLloXPEe+tVMhtr3g1wgjYHJs\n"
          + "9/nu7+3dIrnvhhQWBDZXECBIAzpwdCETCjjMkXQKg93N1ROhNjd/ESOHOrmulTuZ\n"
          + "U81yVGWRbDLzsSmpCTWjU4LmTgk7FDEXiZ3Pihprv7wevPrXeJURU7Bh6GFP2wYD\n"
          + "dz8aawKCAQEAh8ywEMEImhnJMxT0KuIdI550Ae170KdECycbEpDv9oZVi1+oGIEg\n"
          + "IfnchObMafy94G1ZKM4wRSFaEzLyFScwm60kF1ByXLY/LHsXyRGfzyJO1cO7qrz8\n"
          + "7i0MFNgZJ7gEHomHBAr9t38hkVyq8+DLnFPl7NPYXpIxFp+mALYXyAUHiq4ARav7\n"
          + "EPunUYWurQw2WmtWtl+5ezb6RM0NxTNOrSUvUBGGkhRRN2AWer3fuyu7dnSk1+Ti\n"
          + "u3WMxmWrhFRU6l2+lXLlkdvZX3As1PFj4u9RpL4CJDaNdBpDQfiTL3Q5dkKUq3n3\n"
          + "gtelV2wJQo46sx16F5BFDwsYlUBUkggLiQKCAQEAq2maL0a+jCcuKK9wJws7V2Q6\n"
          + "WMHhswm4//5XDKbY1d0qjJyusyDxD4Xqr72s8EsRv4hRKktgFEdkMizwq5snrMdO\n"
          + "6g/estUWxUjW1Wf3fgW75W2M8CSq3acbg5uirqiFTSR51/pTWhlpzf4GoZX2u4x2\n"
          + "1hKZ7esavfCth8G1xx1KSVouqn6E7WvVUN4XqYvYRHVXYXzgS7t5mrnnEp1fjuFm\n"
          + "JU61HNJIMLrQGgHf7+0gq3Q6gtVka7OsH9GBmrJi9S+lGO9xm+e36QRcZe6z70Gm\n"
          + "NE02d5/qDJxpFUoF8gc/c9bTrdab5gmNOurud/n2j0oxb3t8kmiyxyPaB+xlbA==\n"
          + "-----END RSA PRIVATE KEY-----";

  private static final String JWTKEYS_DECRYPTION =
      "{\"keys\": {\""
          + SIGNING_PUBLIC_SHA1
          + "\": "
          + "{\"purpose\": \"decryption\", "
          + "\"type\": \"public\""
          + ", \"value\": \""
          + SIGNING_PUBLIC_VALUE
          + "\"}, \""
          + ENCRYPTION_PRIVATE_SHA1
          + "\": "
          + "{\"purpose\": \"decryption\", "
          + "\"type\": \"private\""
          + ", \"value\": \""
          + ENCRYPTION_PRIVATE_VALUE
          + "\"}}}";

  @Given("I am about to do a smoke test by going to a contact centre endpoint")
  public void i_am_about_to_do_a_smoke_test_by_going_to_a_contact_centre_endpoint() {
    log.info("About to check that the Contact Centre service is running...");
  }

  @Then("I do the smoke test and receive a response of OK from the contact centre service")
  public void i_do_the_smoke_test_and_receive_a_response_of_OK_from_the_contact_centre_service() {
    try {
      HttpStatus contactCentreStatus = checkContactCentreRunning();
      log.with(contactCentreStatus).info("Smoke Test: The response from " + ccSmokeTestUrl);
      assertEquals(
          "THE CONTACT CENTRE SERVICE MAY NOT BE RUNNING - it does not give a response code of 200",
          HttpStatus.OK,
          contactCentreStatus);
    } catch (ResourceAccessException e) {
      log.error(
          "THE CONTACT CENTRE SERVICE MAY NOT BE RUNNING: A ResourceAccessException has occurred.");
      log.error(e.getMessage());
      fail();
      System.exit(0);
    } catch (Exception e) {
      log.error("THE CONTACT CENTRE SERVICE MAY NOT BE RUNNING: An unexpected has occurred.");
      log.error(e.getMessage());
      fail();
      System.exit(0);
    }
  }

  @Given("I am about to do a smoke test by going to a mock case api endpoint")
  public void i_am_about_to_do_a_smoke_test_by_going_to_a_mock_case_api_endpoint() {
    log.info("About to check that the mock case api service is running...");
  }

  @Then("I do the smoke test and receive a response of OK from the mock case api service")
  public void i_do_the_smoke_test_and_receive_a_response_of_OK_from_the_mock_case_api_service() {
    try {
      HttpStatus mockCaseApiStatus = checkMockCaseApiRunning();
      log.with(mockCaseApiStatus).info("Smoke Test: The response from " + mockCaseSvcSmokeTestUrl);
      assertEquals(
          "THE MOCK CASE API SERVICE MAY NOT BE RUNNING - it does not give a response code of 200",
          HttpStatus.OK,
          mockCaseApiStatus);
    } catch (ResourceAccessException e) {
      log.error(
          "THE MOCK CASE API SERVICE MAY NOT BE RUNNING: A ResourceAccessException has occurred.");
      log.error(e.getMessage());
      fail();
      System.exit(0);
    } catch (Exception e) {
      log.error("THE MOCK CASE API SERVICE MAY NOT BE RUNNING: An unexpected has occurred.");
      log.error(e.getMessage());
      fail();
      System.exit(0);
    }
  }

  @Given("I have a valid case ID {string}")
  public void i_have_a_valid_case_ID(String caseId) {
    this.caseId = caseId;
  }

  @When("I Search cases By case ID {string}")
  public void i_Search_cases_By_case_ID(String showCaseEvents) {
    final UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl)
            .port(ccBasePort)
            .pathSegment("cases")
            .pathSegment(caseId)
            .queryParam("caseEvents", showCaseEvents);
    caseDTO = getRestTemplate().getForObject(builder.build().encode().toUri(), CaseDTO.class);
  }

  @Then("the correct case for my case ID is returned {int}")
  public void the_correct_case_for_my_case_ID_is_returned(Integer uprn) {
    assertNotNull("Case Query Response must not be null", caseDTO);
    assertEquals(
        "Case Query Response UPRN must match", caseDTO.getUprn().getValue(), uprn.longValue());
  }

  @Then("the correct number of events are returned {string} {int}")
  public void the_correct_number_of_events_are_returned(
      String showCaseEvents, Integer expectedCaseEvents) {
    if (!Boolean.parseBoolean(showCaseEvents)) {
      assertNull("Events must be null", caseDTO.getCaseEvents());
    } else {
      assertEquals(
          "Must have the correct number of case events",
          Long.valueOf(expectedCaseEvents),
          Long.valueOf(caseDTO.getCaseEvents().size()));
    }
  }

  @Given("I have an invalid case ID {string}")
  public void i_have_an_invalid_case_ID(String caseId) {
    this.caseId = caseId;
  }

  @When("I Search for cases By case ID")
  public void i_Search_for_cases_By_case_ID() {
    final UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl)
            .port(ccBasePort)
            .pathSegment("cases")
            .pathSegment(caseId);
    try {
      caseDTO = getRestTemplate().getForObject(builder.build().encode().toUri(), CaseDTO.class);
    } catch (HttpClientErrorException httpClientErrorException) {
      this.exception = httpClientErrorException;
    } catch (HttpServerErrorException httpServerErrorException) {
      this.exception = httpServerErrorException;
    }
  }

  @Then("An error is thrown and no case is returned {string}")
  public void an_error_is_thrown_and_no_case_is_returned(String httpError) {
    assertTrue(
        "The correct http status must be returned " + httpError,
        exception.getMessage().trim().contains(httpError));
  }

  @Given("I have a valid UPRN {string}")
  public void i_have_a_valid_UPRN(String uprn) {
    this.uprn = uprn;
  }

  @When("I Search cases By UPRN")
  public void i_Search_cases_By_UPRN() {
    final UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl)
            .port(ccBasePort)
            .pathSegment("cases")
            .pathSegment("uprn")
            .pathSegment(uprn);
    try {
      ResponseEntity<List<CaseDTO>> caseResponse =
          getRestTemplate()
              .exchange(
                  builder.build().encode().toUri(),
                  HttpMethod.GET,
                  null,
                  new ParameterizedTypeReference<List<CaseDTO>>() {});
      caseDTOList = caseResponse.getBody();
    } catch (HttpClientErrorException httpClientErrorException) {
      this.exception = httpClientErrorException;
    }
  }

  @Then("the correct cases for my UPRN are returned {string}")
  public void the_correct_cases_for_my_UPRN_are_returned(String caseIds) {
    final List<String> caseIdList = Arrays.stream(caseIds.split(",")).collect(Collectors.toList());
    caseDTOList.forEach(
        caseDetails -> {
          String caseID = caseDetails.getId().toString().trim();
          assertTrue("case ID must be in case list - ", caseIdList.contains(caseID));
        });
  }

  @Given("I have an invalid UPRN {string}")
  public void i_have_an_invalid_UPRN(String uprn) {
    this.uprn = uprn;
  }

  @When("I Search cases By invalid UPRN")
  public void i_Search_cases_By_invalid_UPRN() {
    exception = null;
    final UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl)
            .port(ccBasePort)
            .pathSegment("cases")
            .pathSegment("uprn")
            .pathSegment(uprn);
    try {
      ResponseEntity<List<CaseDTO>> caseResponse =
          getRestTemplate()
              .exchange(
                  builder.build().encode().toUri(),
                  HttpMethod.GET,
                  null,
                  new ParameterizedTypeReference<List<CaseDTO>>() {});
      caseDTOList = caseResponse.getBody();
    } catch (HttpClientErrorException httpClientErrorException) {
      exception = httpClientErrorException;
    }
  }

  @Then("no cases for my UPRN are returned {string}")
  public void no_cases_for_my_UPRN_are_returned(String httpError) {
    assertNotNull("Should throw an exception", exception);
    assertTrue(
        "Invalid UPRN causes http status " + httpError,
        exception.getMessage() != null && exception.getMessage().contains(httpError));

    assertNull("UPRN response must be null", caseDTOList);
  }

  @Given("the CC advisor has the respondent address")
  public void the_CC_advisor_has_the_respondent_address() {
    log.info(
        "nothing to do here - we can assume that the CC advisor has the respondent's address and its UPRN");
  }

  @Given("the respondent case type is a household")
  public void the_respondent_case_type_is_a_household() {
    log.info("nothing to do here - we can assume that the case type is a household");
  }

  @When("the CC advisor confirms the address")
  public void the_CC_advisor_confirms_the_address() {
    log.info("nothing to do here - the CC advisor clicks a button to confirm the address");
  }

  @When("confirms the CaseType=HH")
  public void confirms_the_CaseType_HH() {
    log.info(
        "The CC advisor clicks a button to confirm that the case type is HH and then launch EQ...");

    try {
      HttpStatus contactCentreStatus = getEqTokenForHH();
      log.with(contactCentreStatus)
          .info("Launch EQ for HH: The response from " + telephoneEndpointUrl);
      assertEquals(
          "LAUNCHING EQ FOR HH HAS FAILED -  the contact centre does not give a response code of 200",
          HttpStatus.OK,
          contactCentreStatus);
    } catch (ResourceAccessException e) {
      log.error("LAUNCHING EQ FOR HH HAS FAILED: A ResourceAccessException has occurred.");
      log.error(e.getMessage());
      fail();
      System.exit(0);
    } catch (Exception e) {
      log.error("LAUNCHING EQ FOR HH HAS FAILED: An unexpected has occurred.");
      log.error(e.getMessage());
      fail();
      System.exit(0);
    }
  }

  @Then("a HH EQ is launched")
  public void a_HH_EQ_is_launched() throws CTPException {
    String hhEqToken;

    log.info(
        "Create a substring that removes the first part of the telephoneEndpointBody to leave just the EQ token value");

    hhEqToken = telephoneEndpointBody.substring(37);

    log.info("The EQ token is: " + hhEqToken);

    EQJOSEProvider coderDecoder = new Codec();

    KeyStore keyStoreDecryption = new KeyStore(JWTKEYS_DECRYPTION);

    JWEHelper jweHelper = new JWEHelper();

    String kid = jweHelper.getKid(hhEqToken);

    log.info("The kid is: " + kid);

    //    String decryptedEqToken = coderDecoder.decrypt(hhEqToken, keyStoreDecryption);
    //
    //    log.info("The decrypted String is: " + decryptedEqToken);
  }

  private HttpStatus checkContactCentreRunning() {
    log.info("Entering checkContactCentreRunning method");
    final UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl).port(ccBasePort).pathSegment("fulfilments");

    ccSmokeTestUrl = builder.build().encode().toUri().toString();

    log.info(
        "Using the following endpoint to check that the contact centre service is running: "
            + ccSmokeTestUrl);

    ResponseEntity<List<FulfilmentDTO>> fulfilmentResponse =
        getRestTemplate()
            .exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<FulfilmentDTO>>() {});

    return fulfilmentResponse.getStatusCode();
  }

  private HttpStatus checkMockCaseApiRunning() {
    log.info("Entering checkMockCaseApiRunning method");
    final UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(mcsBaseUrl)
            .port(mcsBasePort)
            .pathSegment("cases")
            .pathSegment("info");

    RestTemplate restTemplate = getAuthenticationFreeRestTemplate();

    mockCaseSvcSmokeTestUrl = builder.build().encode().toUri().toString();

    log.info(
        "Using the following endpoint to check that the mock case api service is running: "
            + mockCaseSvcSmokeTestUrl);

    ResponseEntity<String> mockCaseApiResponse =
        restTemplate.getForEntity(builder.build().encode().toUri(), String.class);

    return mockCaseApiResponse.getStatusCode();
  }

  private HttpStatus getEqTokenForHH() {
    final UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl)
            .port(ccBasePort)
            .pathSegment("cases")
            .pathSegment("3305e937-6fb1-4ce1-9d4c-077f147789ab")
            .pathSegment("launch")
            .queryParam("agentId", 1)
            .queryParam("individual", false);

    telephoneEndpointUrl = builder.build().encode().toUri().toString();

    log.info("Using the following endpoint to launch EQ for HH: " + telephoneEndpointUrl);

    ResponseEntity<String> ccLaunchEqResponse =
        getRestTemplate().getForEntity(builder.build().encode().toUri(), String.class);

    telephoneEndpointBody = ccLaunchEqResponse.getBody();

    return ccLaunchEqResponse.getStatusCode();
  }
}
