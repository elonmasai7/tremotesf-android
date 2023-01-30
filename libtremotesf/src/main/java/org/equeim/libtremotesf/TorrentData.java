/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.2.0
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.equeim.libtremotesf;

public class TorrentData {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected TorrentData(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(TorrentData obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(TorrentData obj) {
    long ptr = 0;
    if (obj != null) {
      if (!obj.swigCMemOwn)
        throw new RuntimeException("Cannot release ownership as memory is not owned");
      ptr = obj.swigCPtr;
      obj.swigCMemOwn = false;
      obj.delete();
    }
    return ptr;
  }

  @SuppressWarnings("deprecation")
  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtremotesfJNI.delete_TorrentData(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public int getId() {
    return libtremotesfJNI.TorrentData_id_get(swigCPtr, this);
  }

  public String getHashString() {
    return libtremotesfJNI.TorrentData_hashString_get(swigCPtr, this);
}

  public String getName() {
    return libtremotesfJNI.TorrentData_name_get(swigCPtr, this);
}

  public String getMagnetLink() {
    return libtremotesfJNI.TorrentData_magnetLink_get(swigCPtr, this);
}

  public TorrentData.Status getStatus() {
    return TorrentData.Status.swigToEnum(libtremotesfJNI.TorrentData_status_get(swigCPtr, this));
  }

  public TorrentData.Error getError() {
    return TorrentData.Error.swigToEnum(libtremotesfJNI.TorrentData_error_get(swigCPtr, this));
  }

  public String getErrorString() {
    return libtremotesfJNI.TorrentData_errorString_get(swigCPtr, this);
}

  public int getQueuePosition() {
    return libtremotesfJNI.TorrentData_queuePosition_get(swigCPtr, this);
  }

  public long getTotalSize() {
    return libtremotesfJNI.TorrentData_totalSize_get(swigCPtr, this);
  }

  public long getCompletedSize() {
    return libtremotesfJNI.TorrentData_completedSize_get(swigCPtr, this);
  }

  public long getLeftUntilDone() {
    return libtremotesfJNI.TorrentData_leftUntilDone_get(swigCPtr, this);
  }

  public long getSizeWhenDone() {
    return libtremotesfJNI.TorrentData_sizeWhenDone_get(swigCPtr, this);
  }

  public double getPercentDone() {
    return libtremotesfJNI.TorrentData_percentDone_get(swigCPtr, this);
  }

  public double getRecheckProgress() {
    return libtremotesfJNI.TorrentData_recheckProgress_get(swigCPtr, this);
  }

  public int getEta() {
    return libtremotesfJNI.TorrentData_eta_get(swigCPtr, this);
  }

  public boolean getMetadataComplete() {
    return libtremotesfJNI.TorrentData_metadataComplete_get(swigCPtr, this);
  }

  public long getDownloadSpeed() {
    return libtremotesfJNI.TorrentData_downloadSpeed_get(swigCPtr, this);
  }

  public long getUploadSpeed() {
    return libtremotesfJNI.TorrentData_uploadSpeed_get(swigCPtr, this);
  }

  public boolean getDownloadSpeedLimited() {
    return libtremotesfJNI.TorrentData_downloadSpeedLimited_get(swigCPtr, this);
  }

  public int getDownloadSpeedLimit() {
    return libtremotesfJNI.TorrentData_downloadSpeedLimit_get(swigCPtr, this);
  }

  public boolean getUploadSpeedLimited() {
    return libtremotesfJNI.TorrentData_uploadSpeedLimited_get(swigCPtr, this);
  }

  public int getUploadSpeedLimit() {
    return libtremotesfJNI.TorrentData_uploadSpeedLimit_get(swigCPtr, this);
  }

  public long getTotalDownloaded() {
    return libtremotesfJNI.TorrentData_totalDownloaded_get(swigCPtr, this);
  }

  public long getTotalUploaded() {
    return libtremotesfJNI.TorrentData_totalUploaded_get(swigCPtr, this);
  }

  public double getRatio() {
    return libtremotesfJNI.TorrentData_ratio_get(swigCPtr, this);
  }

  public double getRatioLimit() {
    return libtremotesfJNI.TorrentData_ratioLimit_get(swigCPtr, this);
  }

  public TorrentData.RatioLimitMode getRatioLimitMode() {
    return TorrentData.RatioLimitMode.swigToEnum(libtremotesfJNI.TorrentData_ratioLimitMode_get(swigCPtr, this));
  }

  public int getTotalSeedersFromTrackersCount() {
    return libtremotesfJNI.TorrentData_totalSeedersFromTrackersCount_get(swigCPtr, this);
  }

  public int getPeersSendingToUsCount() {
    return libtremotesfJNI.TorrentData_peersSendingToUsCount_get(swigCPtr, this);
  }

  public StringsVector getWebSeeders() {
    long cPtr = libtremotesfJNI.TorrentData_webSeeders_get(swigCPtr, this);
    return (cPtr == 0) ? null : new StringsVector(cPtr, false);
  }

  public int getWebSeedersSendingToUsCount() {
    return libtremotesfJNI.TorrentData_webSeedersSendingToUsCount_get(swigCPtr, this);
  }

  public int getTotalLeechersFromTrackersCount() {
    return libtremotesfJNI.TorrentData_totalLeechersFromTrackersCount_get(swigCPtr, this);
  }

  public int getPeersGettingFromUsCount() {
    return libtremotesfJNI.TorrentData_peersGettingFromUsCount_get(swigCPtr, this);
  }

  public int getPeersLimit() {
    return libtremotesfJNI.TorrentData_peersLimit_get(swigCPtr, this);
  }

  public @androidx.annotation.Nullable org.threeten.bp.Instant getAddedDate() { return org.equeim.libtremotesf.libtremotesfJNI.millisToInstant(libtremotesfJNI.TorrentData_addedDate_get(swigCPtr, this)); }

  public @androidx.annotation.Nullable org.threeten.bp.Instant getActivityDate() { return org.equeim.libtremotesf.libtremotesfJNI.millisToInstant(libtremotesfJNI.TorrentData_activityDate_get(swigCPtr, this)); }

  public @androidx.annotation.Nullable org.threeten.bp.Instant getDoneDate() { return org.equeim.libtremotesf.libtremotesfJNI.millisToInstant(libtremotesfJNI.TorrentData_doneDate_get(swigCPtr, this)); }

  public TorrentData.IdleSeedingLimitMode getIdleSeedingLimitMode() {
    return TorrentData.IdleSeedingLimitMode.swigToEnum(libtremotesfJNI.TorrentData_idleSeedingLimitMode_get(swigCPtr, this));
  }

  public int getIdleSeedingLimit() {
    return libtremotesfJNI.TorrentData_idleSeedingLimit_get(swigCPtr, this);
  }

  public String getDownloadDirectory() {
    return libtremotesfJNI.TorrentData_downloadDirectory_get(swigCPtr, this);
}

  public String getComment() {
    return libtremotesfJNI.TorrentData_comment_get(swigCPtr, this);
}

  public String getCreator() {
    return libtremotesfJNI.TorrentData_creator_get(swigCPtr, this);
}

  public @androidx.annotation.Nullable org.threeten.bp.Instant getCreationDate() { return org.equeim.libtremotesf.libtremotesfJNI.millisToInstant(libtremotesfJNI.TorrentData_creationDate_get(swigCPtr, this)); }

  public TorrentData.Priority getBandwidthPriority() {
    return TorrentData.Priority.swigToEnum(libtremotesfJNI.TorrentData_bandwidthPriority_get(swigCPtr, this));
  }

  public boolean getHonorSessionLimits() {
    return libtremotesfJNI.TorrentData_honorSessionLimits_get(swigCPtr, this);
  }

  public boolean getSingleFile() {
    return libtremotesfJNI.TorrentData_singleFile_get(swigCPtr, this);
  }

  public TrackersVector getTrackers() {
    long cPtr = libtremotesfJNI.TorrentData_trackers_get(swigCPtr, this);
    return (cPtr == 0) ? null : new TrackersVector(cPtr, false);
  }

  public boolean hasError() {
    return libtremotesfJNI.TorrentData_hasError(swigCPtr, this);
  }

  public boolean isFinished() {
    return libtremotesfJNI.TorrentData_isFinished(swigCPtr, this);
  }

  public boolean isDownloadingStalled() {
    return libtremotesfJNI.TorrentData_isDownloadingStalled(swigCPtr, this);
  }

  public boolean isSeedingStalled() {
    return libtremotesfJNI.TorrentData_isSeedingStalled(swigCPtr, this);
  }

  public enum Status {
    Paused,
    QueuedForChecking,
    Checking,
    QueuedForDownloading,
    Downloading,
    QueuedForSeeding,
    Seeding;

    public final int swigValue() {
      return swigValue;
    }

    public static Status swigToEnum(int swigValue) {
      Status[] swigValues = Status.class.getEnumConstants();
      if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
        return swigValues[swigValue];
      for (Status swigEnum : swigValues)
        if (swigEnum.swigValue == swigValue)
          return swigEnum;
      throw new IllegalArgumentException("No enum " + Status.class + " with value " + swigValue);
    }

    @SuppressWarnings("unused")
    private Status() {
      this.swigValue = SwigNext.next++;
    }

    @SuppressWarnings("unused")
    private Status(int swigValue) {
      this.swigValue = swigValue;
      SwigNext.next = swigValue+1;
    }

    @SuppressWarnings("unused")
    private Status(Status swigEnum) {
      this.swigValue = swigEnum.swigValue;
      SwigNext.next = this.swigValue+1;
    }

    private final int swigValue;

    private static class SwigNext {
      private static int next = 0;
    }
  }

  public enum Error {
    None,
    TrackerWarning,
    TrackerError,
    LocalError;

    public final int swigValue() {
      return swigValue;
    }

    public static Error swigToEnum(int swigValue) {
      Error[] swigValues = Error.class.getEnumConstants();
      if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
        return swigValues[swigValue];
      for (Error swigEnum : swigValues)
        if (swigEnum.swigValue == swigValue)
          return swigEnum;
      throw new IllegalArgumentException("No enum " + Error.class + " with value " + swigValue);
    }

    @SuppressWarnings("unused")
    private Error() {
      this.swigValue = SwigNext.next++;
    }

    @SuppressWarnings("unused")
    private Error(int swigValue) {
      this.swigValue = swigValue;
      SwigNext.next = swigValue+1;
    }

    @SuppressWarnings("unused")
    private Error(Error swigEnum) {
      this.swigValue = swigEnum.swigValue;
      SwigNext.next = this.swigValue+1;
    }

    private final int swigValue;

    private static class SwigNext {
      private static int next = 0;
    }
  }

  public enum Priority {
    Low,
    Normal,
    High;

    public final int swigValue() {
      return swigValue;
    }

    public static Priority swigToEnum(int swigValue) {
      Priority[] swigValues = Priority.class.getEnumConstants();
      if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
        return swigValues[swigValue];
      for (Priority swigEnum : swigValues)
        if (swigEnum.swigValue == swigValue)
          return swigEnum;
      throw new IllegalArgumentException("No enum " + Priority.class + " with value " + swigValue);
    }

    @SuppressWarnings("unused")
    private Priority() {
      this.swigValue = SwigNext.next++;
    }

    @SuppressWarnings("unused")
    private Priority(int swigValue) {
      this.swigValue = swigValue;
      SwigNext.next = swigValue+1;
    }

    @SuppressWarnings("unused")
    private Priority(Priority swigEnum) {
      this.swigValue = swigEnum.swigValue;
      SwigNext.next = this.swigValue+1;
    }

    private final int swigValue;

    private static class SwigNext {
      private static int next = 0;
    }
  }

  public enum RatioLimitMode {
    Global,
    Single,
    Unlimited;

    public final int swigValue() {
      return swigValue;
    }

    public static RatioLimitMode swigToEnum(int swigValue) {
      RatioLimitMode[] swigValues = RatioLimitMode.class.getEnumConstants();
      if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
        return swigValues[swigValue];
      for (RatioLimitMode swigEnum : swigValues)
        if (swigEnum.swigValue == swigValue)
          return swigEnum;
      throw new IllegalArgumentException("No enum " + RatioLimitMode.class + " with value " + swigValue);
    }

    @SuppressWarnings("unused")
    private RatioLimitMode() {
      this.swigValue = SwigNext.next++;
    }

    @SuppressWarnings("unused")
    private RatioLimitMode(int swigValue) {
      this.swigValue = swigValue;
      SwigNext.next = swigValue+1;
    }

    @SuppressWarnings("unused")
    private RatioLimitMode(RatioLimitMode swigEnum) {
      this.swigValue = swigEnum.swigValue;
      SwigNext.next = this.swigValue+1;
    }

    private final int swigValue;

    private static class SwigNext {
      private static int next = 0;
    }
  }

  public enum IdleSeedingLimitMode {
    Global,
    Single,
    Unlimited;

    public final int swigValue() {
      return swigValue;
    }

    public static IdleSeedingLimitMode swigToEnum(int swigValue) {
      IdleSeedingLimitMode[] swigValues = IdleSeedingLimitMode.class.getEnumConstants();
      if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
        return swigValues[swigValue];
      for (IdleSeedingLimitMode swigEnum : swigValues)
        if (swigEnum.swigValue == swigValue)
          return swigEnum;
      throw new IllegalArgumentException("No enum " + IdleSeedingLimitMode.class + " with value " + swigValue);
    }

    @SuppressWarnings("unused")
    private IdleSeedingLimitMode() {
      this.swigValue = SwigNext.next++;
    }

    @SuppressWarnings("unused")
    private IdleSeedingLimitMode(int swigValue) {
      this.swigValue = swigValue;
      SwigNext.next = swigValue+1;
    }

    @SuppressWarnings("unused")
    private IdleSeedingLimitMode(IdleSeedingLimitMode swigEnum) {
      this.swigValue = swigEnum.swigValue;
      SwigNext.next = this.swigValue+1;
    }

    private final int swigValue;

    private static class SwigNext {
      private static int next = 0;
    }
  }

  public enum UpdateKey {
  ;

    public final int swigValue() {
      return swigValue;
    }

    public static UpdateKey swigToEnum(int swigValue) {
      UpdateKey[] swigValues = UpdateKey.class.getEnumConstants();
      if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
        return swigValues[swigValue];
      for (UpdateKey swigEnum : swigValues)
        if (swigEnum.swigValue == swigValue)
          return swigEnum;
      throw new IllegalArgumentException("No enum " + UpdateKey.class + " with value " + swigValue);
    }

    @SuppressWarnings("unused")
    private UpdateKey() {
      this.swigValue = SwigNext.next++;
    }

    @SuppressWarnings("unused")
    private UpdateKey(int swigValue) {
      this.swigValue = swigValue;
      SwigNext.next = swigValue+1;
    }

    @SuppressWarnings("unused")
    private UpdateKey(UpdateKey swigEnum) {
      this.swigValue = swigEnum.swigValue;
      SwigNext.next = this.swigValue+1;
    }

    private final int swigValue;

    private static class SwigNext {
      private static int next = 0;
    }
  }

}
