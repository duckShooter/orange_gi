package gi.orange.task;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

@Component
public class Loader implements CommandLineRunner {

	@Autowired
	private JdbcOperations jdbcTemplate;
	
	@Autowired
	private DataSource dataSource;
	
	private final String CHECK_DATA_EXIST = "SELECT EXISTS (SELECT NULL FROM %s)";
	private final String INSERT_USER = "INSERT IGNORE INTO user (username, password) VALUES (?, ?)";
	
	@Override
	public void run(String... args) throws Exception {
		Logger log = LoggerFactory.getLogger(this.getClass());
		jdbcTemplate.update(INSERT_USER, "user", "$2a$10$a8r484Ht4fOSYUbVR3mZZOlMOEJu17PuRakkCBz07dSxrWifU.krK"); //user, secret
		
		byte tableHasData = jdbcTemplate.queryForObject(String.format(CHECK_DATA_EXIST, "category"), Byte.class);
		
		/* Check if data already exist to avoid duplicate insertion of data on different re-runs.
		 * According to the imposed constraints , if category table is empty, product table is also empty. 
		 */
		if(tableHasData == 0) { //Table is empty, proceed with table population
			log.info("Populating database tables with data ...");
			ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("sql/script.sql"));
		}
	}
}
