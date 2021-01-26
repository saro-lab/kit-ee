### DOCS
- [SARO KIT](https://github.com/saro-lab/kit)
  - Functions,
    Files,
    List,
    Numbers
    Primitive
    Streams,
    Texts,
    Threads,
    Valids,
    Koreans

- [SARO KIT Enterprise Edition](https://github.com/saro-lab/kit-ee)
  - [FTP](https://github.com/saro-lab/kit-ee/blob/master/docs/FTP.md),
    [SFTP](https://github.com/saro-lab/kit-ee/blob/master/docs/SFTP.md)
  
# FTP
준비중


## kotlin example
- FTP with custom options
  ```kotlin
   Ftp.ftp("test.rebex.net", 21, "demo", "password")
        .beforeLogin { it.connectTimeout = 60000 }
        .encoding("EUC-KR")
        .open()
        .use { ftp ->
            println("ftp opened!!")
            println("pwd: ${ftp.pwd()}")
            ftp.listFiles().forEach { println("file: $it") }
            assert(true)
        }
  ```
- FTPS (explicit mode)
  ```kotlin
  Ftp.ftps(false, "test.rebex.net", 21, "demo", "password")
            .open()
            .use { ftp ->
                println("ftps (explicit mode) opened!!")
                println("pwd: ${ftp.pwd()}")
                ftp.listFiles().forEach { println("file: $it") }
                assert(true)
            }
  ```
- FTPS (implicit mode)
  ```kotlin
  Ftp.ftps(true, "test.rebex.net", 990, "demo", "password")
      .open()
      .use { ftp ->
          println("ftps (implicit mode) opened!!")
          println("pwd: ${ftp.pwd()}")
          ftp.listFiles().forEach { println("file: $it") }
          assert(true)
      }
  ```

## java example
- FTP
  ```java
  try (Ftp ftp = Ftp.ftp("test.rebex.net", 21, "demo", "password").open()) {
      System.out.println("ftp opened!!");
      System.out.println("pwd: " + ftp.pwd());
      ftp.listFiles().forEach( it -> System.out.println("file: " + it) );
      assert(true);
  }
  ```
- FTPS (explicit mode)
  ```java
  try (Ftp ftp = Ftp.ftps(false, "test.rebex.net", 21, "demo", "password").open()) {
      System.out.println("ftps (explicit mode) opened!!");
      System.out.println("pwd: " + ftp.pwd());
      ftp.listFiles().forEach( it -> System.out.println("file: " + it) );
      assert(true);
  }
  ```
- FTPS (implicit mode) with custom options
  ```java
  try (
          Ftp ftp = Ftp
                  .ftps(true, "test.rebex.net", 990, "demo", "password")
                  .encoding("UTF-8")
                  .open()
  ) {
      System.out.println("ftps (implicit mode) opened!!");
      System.out.println("pwd: " + ftp.pwd());
      ftp.listFiles().forEach( it -> System.out.println("file: " + it) );
      assert(true);
  }
  ```

## 참고
- [kotlin test code](https://github.com/saro-lab/kit-ee/blob/master/src/test/kotlin/me/saro/test/kit/ee/kt/FtpTest.kt)
- [java text code](https://github.com/saro-lab/kit-ee/blob/master/src/test/java/me/saro/test/kit/ee/FtpTest.java)
