package io.rishabh.bookshelf_server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureRestDocs(outputDir = "./target/snippets")
class BookshelfServerApplicationTests {

	@Test
	void contextLoads() {
	}

	
}
