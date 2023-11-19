# How to use Object Storage API

<br/>

- This document describes how to use the Object Storage API served by Naver Cloud.
- Areas of use include uploading video files, json files, and thumbnail files.
- No need to use Naver Cloud and is compatible with AWS S3.
- Refer to Naver Cloud’s [AWS SDK Guide for Java](https://guide.ncloud-docs.com/docs/storage-storage-8-1).
- Last Edit: 2023.10.02

# outline

* [Why should I use it?](#Why-should-i-use-it)
* [Architecture](#Architecture)
* [Access-key and Secret-key issuance](#1-access-key-and-secret-key-issuance)
* [Create Object Storage bucket](#2-create-object-storage-bucket)
* [S3 sdk dependency injection through gradle](#3-s3-sdk-dependency-injection-through-gradle)
* [Fill in application.yml](#4-Fill-in-applicationyml)
* [Creating S3Client configuration file](#5-creating-s3client-configuration-file)
* [Description of API usage](#6-description-of-api-usage)
    - [Dependency Injection](#6-1-Dependency-Injection)
    - [upload implementation](#6-2-upload-implementation)
    - [delete implementation](#6-3-delete-implementation)
* [Starting S3 Client through Test Code](#7-starting-s3-client-through-test-code)
    - [Dependency Injection](#7-1-Dependency-Injection)
    - [Bucket list search](#7-2-bucket-list-search)
    - [Upload files to bucket](#7-3-upload-files-to-bucket)
    - [Delete files from bucket](#7-4-delete-files-from-bucket)

## Why should I use it?

The reasons for using Object Storage

- **Efficient data management:** The open source we created exchanges data with both the client and the AI ​​server. If
  the files sent from the client to the BE server are stored in Object Storage, they can be used by the AI ​​Server and
  the client, reducing the number of API requests received from BE.
- **Reduced Latency:** Since files can be accessed in URL format, latency through file transfer is reduced.
- **Server capacity and management efficiency:** When files are stored inside the server, server capacity increases and
  file management becomes difficult when the server is expanded. These problems can be solved through Object Storage.
- **Consistent file management:** Like Redis' Global Cache, consistent data processing is possible by managing all files
  in one place.

<br/>

## Architecture

![image](https://github.com/MotuS-Web/MotuS-Backend/assets/52206904/d218f932-a18e-41c9-a5c7-a22643d0653d)

## 1. Access-key and Secret-key issuance

- Log in to NaverCloud -> My Page -> Authentication Key Management -> Create a new API authentication key

## 2. Create Object Storage bucket

> [Object Storage Screen Guide](https://guide.ncloud-docs.com/docs/objectstorage-use-screen)

- Console -> Services -> Object Storage -> Bucket Management -> Create Bucket
- Create a new folder (create folders that will be used in the future)
- in our case, create three folders called video, json, and thumbnail

![image](https://github.com/MotuS-Web/MotuS-Backend/assets/52206904/9f6ce919-f691-4488-b562-919b388f85e4)
<br/>

## 3. S3 sdk dependency injection through gradle

```gradle
implementation 'com.amazonaws:aws-java-sdk-s3:1.11.238'
```

## 4. Fill in application.yml

- Enter the issued key information, region, bucket name, etc. in spring boot’s application.yml.
- Since this is sensitive information, we use the method of creating a separate configuration file and loading it from
  the main yml.

```yml
cloud:
  aws:
    credentials:
      access-key: "your access-key"
      secret-key: "your secret-key"
    stack:
      auto: false
    region:
      static: ap-northeast-2 // your location -> if you in korea, use ap-northeast-2
    s3:
      endpoint: https://kr.object.ncloudstorage.com
      bucket: "your bucket name"
```

## 5. Creating S3Client configuration file

- Create a configuration file through @Configuration. (To manage as a singleton)
- Since Amazon's S3 SDK is used, an object to connect to AmazonS3 is created and returned.
- Use @Value to load information from a configuration file written in yml.

``` java
@Configuration
public class S3Client {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.s3.endpoint}")
    private String endPoint;

    @Value("${cloud.aws.region.static}")
    private String regionName;

    public AmazonS3 getAmazonS3() {
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }
}
```

## 6. Description of API usage

> [Video Service implementation code](https://github.com/MotuS-Web/MotuS-Backend/blob/main/src/main/java/com/hallym/rehab/domain/video/service/VideoServiceImpl.java)

- Difference between Path and URL
- Path is the path where files are actually stored in Object Storage, and when deleted, this is used to delete them.
- URL is the URL used when the user actually wants to obtain the Object. This is not provided by the S3 SDK, but since
  the pattern is consistent, we create and store it ourselves.

### 6-1 Dependency Injection

```java
@Value("${cloud.aws.s3.bucket}")
private String bucketName; // your bucketName
private final S3Client s3Client; // connect to S3
private final VideoRepository videoRepository;
```

### 6-2. upload implementation

1. In the createVideo method part, receive files to upload as MultipartFile.
2. Create an uploadFileToS3 method and pass video files and json files as parameters.

```java
@Override
public String createVideo(VideoRequestDTO videoRequestDTO) {
    MultipartFile[] files = videoRequestDTO.getFiles();

    MultipartFile videoFile = files[0];
    MultipartFile jsonFile = files[1];
    UploadFileDTO uploadVideoResponseDTO = uploadFileToS3(videoFile, jsonFile);

    ...The following codes are explained in number 8.
}
```

3. Create a singleton object using getAmazonS3 of the s3Client that received DI.

```java
AmazonS3 s3 = s3Client.getAmazonS3();
```

4. Convert MultipartFile to File object through the method that converts it to File object.

```java
uploadVideoFile = convertMultipartFileToFile(videoFile, videoFileName);
uploadJsonFile = convertMultipartFileToFile(jsonFile, jsonFileName);
...

@Override
public File convertMultipartFileToFile(MultipartFile multipartFile, String fileName) {
    File convertedFile = new File(fileName);
    try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
        fos.write(multipartFile.getBytes());
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
    return convertedFile;
}

```

5. Upload the file by specifying a path to the desired bucket using the s3.putObject method.

```java
s3.putObject(bucketName, VideoObjectPath, uploadVideoFile);
s3.putObject(bucketName, jsonObjectPath, uploadJsonFile);
s3.putObject(bucketName, thumbnailObjectPath, uploadThumbnailFile);
```

> [Object Storage Permission Management](https://guide.ncloud-docs.com/docs/storage-objectstorage-subaccount)

6. When first creating files, The user's read access will be disabled.
7. Implement the setACL method to make the files accessible to AllUsers.

```java
// All Users can access Object
setAcl(s3, VideoObjectPath);
setAcl(s3, jsonObjectPath);
setAcl(s3, thumbnailObjectPath);

@Override
public void setAcl(AmazonS3 s3, String objectPath) {
    AccessControlList objectAcl = s3.getObjectAcl(bucketName, objectPath);
    objectAcl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
    s3.setObjectAcl(bucketName, objectPath, objectAcl);
}

```

8. Create URL, Path, etc. and save the Video Entity.

```java
@Override
public String createVideo(VideoRequestDTO videoRequestDTO) {
    MultipartFile[] files = videoRequestDTO.getFiles();

    MultipartFile videoFile = files[0];
    MultipartFile jsonFile = files[1];
    UploadFileDTO uploadVideoResponseDTO = uploadFileToS3(videoFile, jsonFile);

    Video video = videoRequestDTO.toVideo(uploadVideoResponseDTO);
    videoRepository.save(video);

    return "success";
}
```

### 6-3 delete implementation

1. Receive the PK of the video.
2. Obtain the Video object using PK and get the path of the files.

```java
@Override
public String deleteVideo(Long vno) {
    Optional<Video> byId = videoRepository.findById(vno);
    if (byId.isEmpty()) return "Video not found for Id : " + vno;

    Video video = byId.get();
    String videoPath = video.getVideoPath();
    String jsonPath = video.getJsonPath();
    String thumbnailPath = video.getThumbnailPath();

    deleteFileFromS3(videoPath, jsonPath, thumbnailPath);
    ...explained in number 4.
}

```

3. Create a deleteFileFromS3 method and delete it from Object Storage using Path.

```java
@Override
public void deleteFileFromS3(String videoPath, String jsonPath, String thumbnailPath) {
    AmazonS3 s3 = s3Client.getAmazonS3();

    try {
        s3.deleteObject(bucketName, videoPath);
        s3.deleteObject(bucketName, jsonPath);
        log.info("Delete Object successfully");
    } catch(SdkClientException e) {
        e.printStackTrace();
        log.info("Error deleteFileFromS3");
    }
}
```

4. Also delete the Video object.

```java
@Override
public String deleteVideo(Long vno) {
    Optional<Video> byId = videoRepository.findById(vno);
    if (byId.isEmpty()) return "Video not found for Id : " + vno;

    Video video = byId.get();
    String videoPath = video.getVideoPath();
    String jsonPath = video.getJsonPath();
    String thumbnailPath = video.getThumbnailPath();

    deleteFileFromS3(videoPath, jsonPath, thumbnailPath);
    videoRepository.delete(video); // --> delete from DB

    return "success";
}
```

## 7. Starting S3 Client through Test Code

> [Test Code](https://github.com/MotuS-Web/MotuS-Backend/blob/main/src/test/java/com/hallym/rehab/global/config/S3ClientTest.java)

### 7-1. dependency injection

1. S3Client dependency injection
2. bucketName declaration

```java
@Autowired
S3Client s3Client;
@Value("${cloud.aws.s3.bucket}")
private String bucketName; // your bucketName
```

### 7-2. Bucket list search

1. List the buckets for the account set in s3Client.

```java
@Test
public void bucketList() {
    AmazonS3 s3 = s3Client.getAmazonS3();

    try {
        List<Bucket> buckets = s3.listBuckets();
        System.out.println("Bucket List: ");
        for (Bucket bucket : buckets) {
            System.out.println(" name=" + bucket.getName() + ", creation_date=" + bucket.getCreationDate() + ", owner=" + bucket.getOwner().getId());
        }
    } catch (AmazonS3Exception e) {
        e.printStackTrace();
    } catch(SdkClientException e) {
        e.printStackTrace();
    }
}
```

### 7-3. Upload files to bucket

1. Upload the sample file located locally.
2. Unlike service logic, you can directly create a File object using filePath.

```java
@Test
public void uploadFile() {
    AmazonS3 s3 = s3Client.getAmazonS3();

    String bucketName = "your bucketName";
    // upload local file
    String objectPath = "video/video1";
    String filePath = "src/main/resources/sample.mp4";

    try {
        s3.putObject(bucketName, objectPath, new File(filePath));
        System.out.format("Object %s has been created.\n", objectPath);
    } catch (AmazonS3Exception e) {
        e.printStackTrace();
    } catch(SdkClientException e) {
        e.printStackTrace();
    }
}
```

### 7-4. Delete files from bucket

1. Delete the file from the bucket using objectPath.

```java
@Test
public void deleteFile() {
    AmazonS3 s3 = s3Client.getAmazonS3();

    String bucketName = "your bucketName";
    String objectPath = "video/video1";

    //delete object
    try {
        s3.deleteObject(bucketName, objectPath);
        System.out.format("Object %s has been deleted.\n", objectPath);
    } catch (AmazonS3Exception e) {
        e.printStackTrace();
    } catch(SdkClientException e) {
        e.printStackTrace();
    }
}
```
