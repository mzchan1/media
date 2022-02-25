/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.media3.transformer.mh;

import static androidx.media3.transformer.AndroidTestUtil.runTransformer;
import static com.google.common.truth.Truth.assertThat;

import android.content.Context;
import androidx.media3.common.MimeTypes;
import androidx.media3.transformer.AndroidTestUtil;
import androidx.media3.transformer.SsimHelper;
import androidx.media3.transformer.TestTransformationResult;
import androidx.media3.transformer.TransformationRequest;
import androidx.media3.transformer.Transformer;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Checks transcoding quality. */
@RunWith(AndroidJUnit4.class)
public final class TranscodeQualityTest {
  @Test
  public void singleTranscode_ssimIsGreaterThan95Percent() throws Exception {
    Context context = ApplicationProvider.getApplicationContext();
    Transformer transformer =
        new Transformer.Builder(context)
            .setTransformationRequest(
                new TransformationRequest.Builder()
                    .setVideoMimeType(MimeTypes.VIDEO_H265)
                    .setAudioMimeType(MimeTypes.AUDIO_AMR_NB)
                    .build())
            .build();

    TestTransformationResult result =
        runTransformer(
            context,
            /* testId= */ "singleTranscode_ssim",
            transformer,
            AndroidTestUtil.MP4_ASSET_URI_STRING,
            /* timeoutSeconds= */ 120);

    assertThat(
            SsimHelper.calculate(
                context,
                AndroidTestUtil.MP4_ASSET_URI_STRING,
                result.filePath,
                SsimHelper.DEFAULT_COMPARISON_INTERVAL))
        .isGreaterThan(0.95);
  }
}
