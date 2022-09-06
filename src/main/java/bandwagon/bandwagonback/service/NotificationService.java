package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.*;
import bandwagon.bandwagonback.dto.NotificationListDto;
import bandwagon.bandwagonback.dto.exception.notfound.FrontmanNotFoundException;
import bandwagon.bandwagonback.dto.subdto.NotificationDto;
import bandwagon.bandwagonback.repository.BandMemberRepository;
import bandwagon.bandwagonback.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final BandMemberRepository bandMemberRepository;

    @Transactional
    public NotificationListDto getNotificationToUser(User user) {
        List<Notification> notifications = notificationRepository.findAllByReceivingUser(user);
        notificationRepository.setIsReadOfAllNotificationsByUser(user);
        List<NotificationDto> notificationDtos = notifications.stream().map(NotificationDto::new).collect(Collectors.toList());
        return new NotificationListDto(notificationDtos);
    }

    public long getUnreadNotificationCount(User user) {
        return notificationRepository.countByReceivingUserAndIsReadFalse(user);
    }

    @Transactional
    public void createNotification(User sendingUser, User receivingUser, NotificationType type) {
        Notification notification = new Notification(sendingUser, receivingUser, type);
        notificationRepository.save(notification);
    }

    @Transactional
    public void createUserToUser(User sendingUser, User receivingUser, NotificationType type) {
        createNotification(sendingUser, receivingUser, type);
    }

    @Transactional
    public void createUserToBand(User sendingUser, Band band, NotificationType type) {
        for (BandMember bandMember : band.getBandMembers()) {
            createNotification(sendingUser, bandMember.getMember(), type);
        }
    }

    @Transactional
    public void createBandToUser(Band band, User receivingUser, NotificationType type) {
        BandMember frontmanBandMember = bandMemberRepository.findByBandAndIsFrontmanTrue(band).orElse(null);
        if (frontmanBandMember == null) {
            throw new FrontmanNotFoundException();
        }
        createNotification(frontmanBandMember.getMember(), receivingUser, type);
    }
}
