package ru.untitled_devs.bot.shared.image;

import com.google.inject.Inject;
import dev.morphia.Datastore;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import ru.untitled_devs.bot.shared.models.Image;
import ru.untitled_devs.bot.shared.repositories.ImageRepo;
import ru.untitled_devs.core.utils.FileUtils;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;

public class ImageService {
	private final S3Client client;
	private final S3Presigner presigner;
	private final String bucketName;
	private final ImageRepo imageRepo;

	@Inject
	public ImageService(URI endpoint, AwsCredentials credentials,
						Region region, String bucketName, Datastore datastore) {
		this.imageRepo = new ImageRepo(datastore);
		this.bucketName = bucketName;

		StaticCredentialsProvider provider = StaticCredentialsProvider.create(credentials);

		client = S3Client.builder()
			.endpointOverride(endpoint)
			.region(region)
			.credentialsProvider(provider)
			.build();

		presigner = S3Presigner.builder()
			.endpointOverride(endpoint)
			.region(region)
			.credentialsProvider(provider)
			.build();
	}

	public Image saveImage(InputStream in, String fileId) throws IOException {
		byte[] bytes = in.readAllBytes();

		String ext = FileUtils.getImageFileExtension(bytes);
		String fileName = fileId + ext;
		String imagesPath = "images/";

		PutObjectRequest request = PutObjectRequest.builder()
			.bucket(bucketName)
			.key(imagesPath + fileName)
			.build();

		client.putObject(request,
			RequestBody.fromBytes(bytes));

		Image img = new Image();
		img.setFileId(fileId);
		img.setPath(imagesPath + fileName);
		return imageRepo.save(img);
	}

	public Image getImage(String fileId) {
		return imageRepo.findByFileId(fileId);
	}

	public URL getImageURL(Image image) {
		GetObjectRequest request = GetObjectRequest.builder()
			.bucket(bucketName)
			.key(image.getPath())
			.build();

		GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
			.getObjectRequest(request)
			.signatureDuration(Duration.ofMinutes(5))
			.build();

		return presigner.presignGetObject(presignRequest).url();
	}

	public PhotoSize getLargestPhotoSize(List<PhotoSize> photos) {
		return photos.stream()
			.max(Comparator.comparing(PhotoSize::getFileSize))
			.orElseThrow(() -> new IllegalStateException("Фото не найдено"));
	}
}
