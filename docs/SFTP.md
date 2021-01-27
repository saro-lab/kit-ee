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

# SFTP
내용 준비중

## kotlin example
- simple
  ```kotlin
  FTP.sftp("test.rebex.net", 22, "demo", "password").open()
    .use { ftp ->
        println("sftp opened!!")
        println("pwd: ${ftp.pwd()}")
        ftp.listFiles().forEach { println("file: $it") }
        assert(true)
    }
  ```
- with custom options
  ```kotlin
  FTP.sftp("test.rebex.net", 22, "demo", "password")
    .beforeConnect { it.timeout = 120000; }
    .open()
    .use { ftp ->
      println("sftp opened!!")
      println("pwd: ${ftp.pwd()}")
      ftp.listFiles().forEach { println("file: $it") }
      assert(true)
    }
  ```
- public key
  ```kotlin
  Ftp.sftp("public-key.test.com", 22)
    .userPublicKey("demo", "/user/.ssh/id_rsa")
    .beforeConnect { it.timeout = 120000; }
    .open()
    .use { ftp ->
        println("sftp opened!!")
        println("pwd: ${ftp.pwd()}")
        ftp.listFiles().forEach { println("file: $it") }
        assert(true)
    }
  ```

## java example
- simple
  ```java
  try (Ftp ftp = Ftp.sftp("test.rebex.net", 22, "demo", "password").open()) {
    System.out.println("sftp opened!!");
    System.out.println("pwd: " + ftp.pwd());
    ftp.listFiles().forEach( it -> System.out.println("file: " + it) );
    assert(true);
  }
  ```
- with custom options
  ```java
  try (
      Ftp ftp = 
          Ftp
              .sftp("test.rebex.net", 22, "demo", "password")
              .beforeConnect { it.timeout = 120000; }
              .open()
  ) {
      System.out.println("sftp opened!!");
      System.out.println("pwd: " + ftp.pwd());
      ftp.listFiles().forEach( it -> System.out.println("file: " + it) );
      assert(true);
  }
  ```
- public key
  ```java
  try (
    Ftp ftp = 
        Ftp
            .sftp("public-key.test.com", 22)
            .userPublicKey("demo", "/user/.ssh/id_rsa")
            .open()
  ) {
      System.out.println("sftp opened!!");
      System.out.println("pwd: " + ftp.pwd());
      ftp.listFiles().forEach( it -> System.out.println("file: " + it) );
      assert(true);
  }
  ```

## 참고
- [kotlin test code](https://github.com/saro-lab/kit-ee/blob/master/src/test/kotlin/me/saro/test/kit/ee/kt/FtpTest.kt)
- [java text code](https://github.com/saro-lab/kit-ee/blob/master/src/test/java/me/saro/test/kit/ee/FtpTest.java)
