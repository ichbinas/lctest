//package com.amazon.urlvending.modifiers;
//
//import com.amazon.atv.encodedassetsprovider.AssetsProvider;
//import com.amazon.atv.encodedassetsprovider.exception.AssetsProviderException;
//import com.amazon.atv.encodedasseturltypes.Asset;
//import com.amazon.atv.encodedasseturltypes.MaterialType;
//import com.amazon.atv.encodedasseturltypes.ResourceType;
//import com.amazon.atv.encodedasseturltypes.StreamingTechnology;
//import com.amazon.atv.encodedasseturltypes.VideoQuality;
//import com.amazon.atv.marketplace.MarketplaceId;
//import com.amazon.atv.urlvending.UrlVendingApp;
//import com.amazon.atv.urlvending.utils.AssetTemplates;
//import com.amazon.atvmanifeststitchingservice.ATVManifestStitchingServiceClient;
//import com.amazon.atvmanifeststitchingservice.GetStitchedManifestUrlsRequest;
//import com.amazon.atvmanifeststitchingservice.GetStitchedManifestUrlsResponse;
//import com.amazon.atvmanifeststitchingservice.StitchedManifestUrlsResponse;
//import com.amazon.atvmanifeststitchingservice.impl.GetStitchedManifestUrlsCall;
//import com.amazon.atvpseudoliveorchestration.ATVPseudoLiveOrchestrationServiceClient;
//import com.amazon.coral.metrics.MetricsFactory;
//import com.amazon.urlvending.AppConfigInitializer;
//import com.amazon.urlvending.Customer;
//import com.amazon.urlvending.Device;
//import com.amazon.urlvending.LinearAsset;
//import com.amazon.urlvending.PlaylistComponent;
//import com.amazon.urlvending.config.FiltersFactory;
//import com.amazon.urlvending.config.UrlVendingServiceRuntimeConfig;
//import com.amazon.urlvending.guidvending.serviceclients.GUIDProvider;
//import com.amazon.urlvending.guidvending.serviceclients.SimpleGUIDProvider;
//import com.amazon.urlvending.modifiers.ManifestStitchingServiceModifier;
//import com.amazon.urlvending.types.Cdn;
//import com.amazon.urlvending.types.DrmType;
//import com.amazon.urlvending.types.RequestContext;
//import com.amazon.urlvending.types.Url;
//import com.amazon.urlvending.utils.CounterMetricsFactory;
//import com.amazon.urlvending.metrics.MetricsContext;
//
//import com.google.common.collect.ImmutableList;
//import com.google.common.collect.ImmutableMap;
//import org.apache.commons.lang.StringUtils;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.invocation.InvocationOnMock;
//import org.mockito.stubbing.Answer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.support.AnnotationConfigContextLoader;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Matchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.reset;
//import static org.mockito.Mockito.when;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
//public class ManifestStitchingServiceModifierTest {
//    @BeforeClass
//    public static void initialize() {
//        /**
//         * App config is initialized usually in the setup methods. However, in
//         * this case, the spring framework calls for instantiating the beans are
//         * performed even before the setup is executed. One of the bin
//         * instantiations requires the AppConfig.
//         */
//        AppConfigInitializer.initialize();
//    }
//
//    @Configuration
//    static class ContextConfiguration {
//        @Bean
//        public UrlVendingServiceRuntimeConfig runtimeConfig() {
//            return new UrlVendingServiceRuntimeConfig();
//        }
//
//        @Bean
//        public UrlVendingApp urlVendingApp() {
//            return mock(UrlVendingApp.class);
//        }
//
//        @Bean
//        public FiltersFactory filtersFactory() {
//            return new FiltersFactory(runtimeConfig());
//        }
//
//        @Bean
//        public ManifestStitchingServiceModifier mssModifier() {
//            return new ManifestStitchingServiceModifier();
//        }
//
//        @Value("${apollo.OCF.UrlVendingService.sableClientName:}")
//        private String sableClientName;
//
//        @Bean
//        public AssetsProvider assetsProvider() throws AssetsProviderException {
//            return mock(AssetsProvider.class);
//        }
//
//        @Bean
//        public ATVPseudoLiveOrchestrationServiceClient pseudoLiveOrchestrationServiceClient() {
//            return mock(ATVPseudoLiveOrchestrationServiceClient.class);
//        }
//
//        @Bean
//        @Qualifier("atvManifestStitchingServiceClient")
//        public ATVManifestStitchingServiceClient mssClient() {
//            return mock(ATVManifestStitchingServiceClient.class);
//        }
//
//        @Bean
//        public GetStitchedManifestUrlsCall mssCall() {
//            return mock(GetStitchedManifestUrlsCall.class);
//        }
//
//        @Bean
//        public MetricsFactory metricsFactory() {
//            return new CounterMetricsFactory();
//        }
//
//        @Bean
//        @Qualifier("guidProvider")
//        public GUIDProvider simpleGUIDProvider() {
//            return mock(SimpleGUIDProvider.class);
//        }
//    }
//
//    @Autowired
//    UrlVendingApp urlVendingApp;
//    @Autowired
//    ATVManifestStitchingServiceClient mssClient;
//    @Autowired
//    GetStitchedManifestUrlsCall mssCall;
//    @Autowired
//    GUIDProvider guidProvider;
//
//    @Autowired
//    ManifestStitchingServiceModifier mssModifier;
//    @Autowired
//    private MetricsFactory metricsFactory;
//
//    private List<String> guids = Arrays.asList("uuid1", "uuid2", "uuid3", "uuid4", "uuid5");
//    private List<String> DASH_GUIDs = Arrays.asList("DASH_UUID1", "DASH_UUID2", "DASH_UUID3", "DASH_UUID4", "DASH_UUID5");
//
//    protected List<Url> urls;
//    protected Map<String, List<Asset>> sableAdAssets;
//    protected Map<String, List<PlaylistComponent>> cuepoints;
//
//    protected String encryptedMarketplaceId;
//    private RequestContext requestContext;
//    private GetStitchedManifestUrlsResponse expectedMssResponse;
//    private StitchedManifestUrlsResponse expectedStitchedResults;
//
//    @SuppressWarnings("unchecked")
//    @Before
//    public void setUp() {
//        MetricsContext.setMetrics(metricsFactory.newMetrics());
//
//        urls = createHLSUrls();
//        sableAdAssets = createHLSAdsAssets();
//        cuepoints = createCuepoints();
//        requestContext = createRequestContext(cuepoints);
//        expectedStitchedResults = createStitchResults();
//        expectedMssResponse = GetStitchedManifestUrlsResponse.builder()
//                .withStitchedManifestUrlsResponse(expectedStitchedResults)
//                .build();
//
//        reset(urlVendingApp, mssClient);
//
//        //Mock 3 dependencies
//        when(urlVendingApp.fetchSableAssets(any(String.class), any(io.vavr.collection.Map.class), any(RequestContext.class), any(String.class), any(ResourceType.class)))
//                .thenAnswer(invocation -> {
//                    String assetKey = (String) invocation.getArguments()[0];
//                    return sableAdAssets.get(assetKey);
//                });
//
//        when(mssClient.newGetStitchedManifestUrlsCall()).thenReturn(mssCall);
//        when(mssCall.call(any(GetStitchedManifestUrlsRequest.class)))
//                .thenReturn(expectedMssResponse);
//
//
//        when(guidProvider.getGUID()).thenAnswer(new Answer() {
//            private int count = 0;
//
//            public Object answer(InvocationOnMock invocation) {
//                return guids.get(count++);
//            }
//        });
//    }
//
//    @After
//    public void after() {
//        MetricsContext.getMetrics().close();
//        MetricsContext.clear();
//        CounterMetricsFactory.assertMetricsClosedProperly();
//    }
//
//    @Test
//    public void testAdInsertion() throws IOException {
//        List<Url> outputUrls = mssModifier.apply(urls, requestContext);
//
//        //Only MainContent url 2 and 4 are returned because they are SSAI supported
//        assertEquals(2, outputUrls.size());
//        assertTrue(outputUrls.stream().allMatch(Url::isSsaiSupported));
//
//        Url url1 = outputUrls.get(0);
//        Url url2 = outputUrls.get(1);
//        if(url1.getUrlString().equals("http://hostname/resource/1$param/2$param/path/to/file2.m3u8")) {
//            assertTrue(url2.getUrlString().equals("http://hostname/resource/1$param/2$param/path/to/file3.m3u8"));
//        } else if (url1.getUrlString().equals("http://hostname/resource/1$param/2$param/path/to/file3.m3u8")) {
//            assertTrue(url2.getUrlString().equals("http://hostname/resource/1$param/2$param/path/to/file2.m3u8"));
//        }
//
//        //Among 5 cuepoints, only ads at "0" and "199" stitched
//        Map<String, List<PlaylistComponent>> modifiedCuepoints = requestContext.mut.getCuepoints();
//        assertEquals(5, modifiedCuepoints.entrySet().size());
//
//        //One ad has no segmentDuration. It does not matter to HLS
//        assertTrue(modifiedCuepoints.get("0").get(0).getLinearAssets().get(0).getUrls().size() == sableAdAssets.get("1").size());
//        assertTrue(modifiedCuepoints.get("0").get(0).getLinearAssets().get(0).isStitched());
//
//        assertTrue(modifiedCuepoints.get("99").get(0).getLinearAssets().get(0).getPlaybackId() == "2");
//        assertTrue(modifiedCuepoints.get("99").get(0).getLinearAssets().get(0).isStitched() == false);
//
//        assertTrue(modifiedCuepoints.get("199").get(0).getLinearAssets().get(0).getUrls().size() == sableAdAssets.get("3").size());
//        assertTrue(modifiedCuepoints.get("199").get(0).getLinearAssets().get(0).isStitched());
//
//        assertTrue(modifiedCuepoints.get("299").get(0).getLinearAssets().get(0).getPlaybackId() == "4");
//        assertTrue(modifiedCuepoints.get("299").get(0).getLinearAssets().get(0).isStitched() == false);
//
//        assertTrue(modifiedCuepoints.get("399").get(0).getLinearAssets().get(0).getPlaybackId() == "5");
//        assertTrue(modifiedCuepoints.get("399").get(0).getLinearAssets().get(0).isStitched() == false);
//    }
//
//    @Test
//    public void testDASHAdInsertion() throws IOException {
//        urls = createDASHUrls();
//        sableAdAssets = createDASHAdsAssets();
//        cuepoints = createCuepoints();
//        requestContext = createRequestContext(cuepoints);
//        expectedStitchedResults = createDASHStitchResults();
//        expectedMssResponse = GetStitchedManifestUrlsResponse.builder()
//                .withStitchedManifestUrlsResponse(expectedStitchedResults)
//                .build();
//
//        List<Url> outputUrls = mssModifier.apply(urls, requestContext);
//
//        //Only MainContent url 2 and 4 are returned because they are SSAI supported
//        assertEquals(2, outputUrls.size());
//        assertTrue(outputUrls.stream().allMatch(Url::isSsaiSupported));
//
//        Url url1 = outputUrls.get(0);
//        Url url2 = outputUrls.get(1);
//        if(url1.getUrlString().equals("http://hostname/resource/1$param/2$param/path/to/file2.m3u8")) {
//            assertTrue(url2.getUrlString().equals("http://hostname/resource/1$param/2$param/path/to/file3.m3u8"));
//        } else if (url1.getUrlString().equals("http://hostname/resource/1$param/2$param/path/to/file3.m3u8")) {
//            assertTrue(url2.getUrlString().equals("http://hostname/resource/1$param/2$param/path/to/file2.m3u8"));
//        }
//
//        //Among 5 cuepoints, only ads at "0" and "199" stitched
//        Map<String, List<PlaylistComponent>> modifiedCuepoints = requestContext.mut.getCuepoints();
//        assertEquals(5, modifiedCuepoints.entrySet().size());
//
//        //One ad has no segmentDuration. It does not matter to HLS
//        assertTrue(modifiedCuepoints.get("0").get(0).getLinearAssets().get(0).getUrls().size() == sableAdAssets.get("1").size());
//        assertTrue(modifiedCuepoints.get("0").get(0).getLinearAssets().get(0).isStitched());
//
//        assertTrue(modifiedCuepoints.get("99").get(0).getLinearAssets().get(0).getPlaybackId() == "2");
//        assertTrue(modifiedCuepoints.get("99").get(0).getLinearAssets().get(0).isStitched() == false);
//
//        assertTrue(modifiedCuepoints.get("199").get(0).getLinearAssets().get(0).getUrls().size() == sableAdAssets.get("3").size());
//        assertTrue(modifiedCuepoints.get("199").get(0).getLinearAssets().get(0).isStitched());
//
//        assertTrue(modifiedCuepoints.get("299").get(0).getLinearAssets().get(0).getPlaybackId() == "4");
//        assertTrue(modifiedCuepoints.get("299").get(0).getLinearAssets().get(0).isStitched() == false);
//
//        assertTrue(modifiedCuepoints.get("399").get(0).getLinearAssets().get(0).getPlaybackId() == "5");
//        assertTrue(modifiedCuepoints.get("399").get(0).getLinearAssets().get(0).isStitched() == false);
//    }
//
//    @Test
//    public void testSSAdInsertion() throws IOException {
//        urls = createSSUrls();
//
//        List<Url> outputUrls = mssModifier.apply(urls, requestContext);
//
//        //No SS processing now.
//        for(int i = 0; i < urls.size(); i++) {
//            assertTrue(StringUtils.equals(urls.get(i).getUrlString(), outputUrls.get(i).getUrlString()));
//            assertTrue(StringUtils.equals(urls.get(i).getAssetId(), outputUrls.get(i).getAssetId()));
//            assertTrue(StringUtils.equals(urls.get(i).getStreamingTechnology(), outputUrls.get(i).getStreamingTechnology()));
//        }
//    }
//
//    private List<Url> createHLSUrls() {
//        encryptedMarketplaceId = MarketplaceId.US;
//        Url tempUrl = Url.builder()
//                .assetId("assetId")
//                .assetType("assetType")
//                .urlString("http://hostname/resource/1$param/path/to/file.m3u8")
//                .drm(DrmType.FAIRPLAY)
//                .streamingTechnology(StreamingTechnology.HLS.name())
//                .cdn(Cdn.akamai)
//                //.interstitialId("i1") //HLS don't have interstitialId
//                .videoDuration(500L)
//                .videoSegmentDurationNs(20020000L)
//                .ssaiSupported(true)
//                .midrollSupported(true)
//                .modifiedForDynamicManifest(true)
//                .build();
//
//        List<Url> hLSUrls = Arrays.asList(
//                tempUrl.toBuilder()
//                        .urlString("http://hostname/resource/1$param/2$param/path/to/file1.m3u8")
//                        .ssaiSupported(false)
//                        .build(),
//                tempUrl.toBuilder()
//                        .urlString("http://hostname/resource/1$param/2$param/path/to/file2.m3u8")
//                        .ssaiSupported(true)
//                        .build(),
//                tempUrl.toBuilder()
//                        .ssaiSupported(false)
//                        .build(),
//                tempUrl.toBuilder()
//                        .urlString("http://hostname/resource/1$param/2$param/path/to/file3.m3u8")
//                        .ssaiSupported(true)
//                        .build());
//        return hLSUrls;
//    }
//
//    //Main Content Urls
//    private List<Url> createDASHUrls() {
//        encryptedMarketplaceId = MarketplaceId.US;
//        Url tempUrl = Url.builder()
//                .assetId("assetId")
//                .assetType("assetType")
//                .urlString("http://hostname/resource/1$param/path/to/file.mpd")
//                .drm(DrmType.WIDEVINE)
//                .streamingTechnology(StreamingTechnology.DASH.name())
//                .cdn(Cdn.akamai)
//                .interstitialId("dash_main_1")
//                .videoDuration(500L)
//                .videoSegmentDurationNs(20020000L)
//                .ssaiSupported(true)
//                .midrollSupported(true)
//                .modifiedForDynamicManifest(true)
//                .build();
//
//        List<Url> dashUrls = Arrays.asList(
//                tempUrl.toBuilder()
//                        .urlString("http://hostname/resource/1$param/2$param/path/to/file1.mpd")
//                        .ssaiSupported(false)
//                        .build(),
//                tempUrl.toBuilder()
//                        .urlString("http://hostname/resource/1$param/2$param/path/to/file2.mpd")
//                        .ssaiSupported(true)
//                        .build(),
//                tempUrl.toBuilder()
//                        .ssaiSupported(false)
//                        .build(),
//                tempUrl.toBuilder()
//                        .urlString("http://hostname/resource/1$param/2$param/path/to/file3.mpd")
//                        .ssaiSupported(true)
//                        .build());
//        return dashUrls;
//    }
//
//    private List<Url> createSSUrls() {
//        encryptedMarketplaceId = MarketplaceId.US;
//        Url tempUrl = Url.builder()
//                .assetId("assetId")
//                .assetType("assetType")
//                .urlString("http://hostname/resource/1$param/path/to/file.mpd")
//                .drm(DrmType.PLAYREADY)
//                .streamingTechnology(StreamingTechnology.SmoothStreaming.name())
//                .cdn(Cdn.akamai)
//                .interstitialId("SS_main_1")
//                .videoDuration(500L)
//                .videoSegmentDurationNs(20020000L)
//                .ssaiSupported(true)
//                .midrollSupported(true)
//                .modifiedForDynamicManifest(true)
//                .build();
//
//        List<Url> ssUrls = Arrays.asList(
//                tempUrl.toBuilder()
//                        .urlString("http://hostname/resource/1$param/2$param/path/to/file1.ism")
//                        .ssaiSupported(false)
//                        .build(),
//                tempUrl.toBuilder()
//                        .urlString("http://hostname/resource/1$param/2$param/path/to/file2.ism")
//                        .ssaiSupported(true)
//                        .build(),
//                tempUrl.toBuilder()
//                        .ssaiSupported(false)
//                        .build(),
//                tempUrl.toBuilder()
//                        .urlString("http://hostname/resource/1$param/2$param/path/to/file3.ism")
//                        .ssaiSupported(true)
//                        .build());
//        return ssUrls;
//    }
//
//    private Map<String, List<Asset>> createHLSAdsAssets(){
//        // Ad assets
//        Map<String, List<Asset>> hLSsableAdAssets = new HashMap<>();
//        Asset tempAsset = AssetTemplates.emptyBuilder()
//                .audioVideoProperties(AssetTemplates.defaultAvBuilder()
//                        //.interstitialIds(ImmutableList.of("i1", "i2"))
//                        .videoSegmentDurationNs(20020000L)
//                        .videoDuration(100L)
//                        .adAlias(333L)
//                        .bitrate(500)
//                        .streamingTechnology(StreamingTechnology.HLS)
//                        .videoDuration(50L)
//                        .build())
//                .locations(AssetTemplates.defaultLocations("l1"))
//                .build();
//
//        // Ad Asset 1 should be stitched
//        hLSsableAdAssets.put("1", ImmutableList.of(
//                tempAsset.toBuilder()
//                        .audioVideoProperties(tempAsset.getAudioVideoProperties().toBuilder()
//                                .videoSegmentDurationNs(20000000L)
//                                .build())
//                        .build(),
//                tempAsset.toBuilder()
//                        .audioVideoProperties(tempAsset.getAudioVideoProperties().toBuilder()
//                                //.interstitialIds(ImmutableList.of("i2", "i3"))
//                                .build())
//                        .locations(AssetTemplates.defaultLocations("l2"))
//                        .build()
//        ));
//
//        // Asset 2 should not be stitiched in as videoSegmentDuration doesn't match
//        hLSsableAdAssets.put("2", ImmutableList.of(
//                tempAsset.toBuilder()
//                        .audioVideoProperties(tempAsset.getAudioVideoProperties().toBuilder()
//                                .videoSegmentDurationNs(20000000L)
//                                .build())
//                        .locations(AssetTemplates.defaultLocations("l21"))
//                        .build(),
//                tempAsset.toBuilder()
//                        .audioVideoProperties(tempAsset.getAudioVideoProperties().toBuilder()
//                                //.interstitialIds(ImmutableList.of("i2", "i3"))
//                                .videoSegmentDurationNs(20000000L)
//                                .build())
//                        .locations(AssetTemplates.defaultLocations("l22"))
//                        .build()
//        ));
//
//        // Asset 3 should be stitiched
//        hLSsableAdAssets.put("3", ImmutableList.of(
//                tempAsset.toBuilder()
//                        .audioVideoProperties(tempAsset.getAudioVideoProperties().toBuilder()
//                                //.interstitialIds(ImmutableList.of("i1"))
//                                .build())
//                        .locations(AssetTemplates.defaultLocations("l31"))
//                        .build(),
//                tempAsset.toBuilder()
//                        .audioVideoProperties(tempAsset.getAudioVideoProperties().toBuilder()
//                                //.interstitialIds(ImmutableList.of("i1"))
//                                .build())
//                        .locations(AssetTemplates.defaultLocations("l32"))
//                        .build()
//        ));
//
//        // Asset 4 should not be stitiched in as missing videoDuration or videoDuration longer than main content
//        hLSsableAdAssets.put("4", ImmutableList.of(
//                tempAsset.toBuilder()
//                        .audioVideoProperties(tempAsset.getAudioVideoProperties().toBuilder()
//                                .videoDuration(null)
//                                .build())
//                        .build(),
//                tempAsset.toBuilder()
//                        .audioVideoProperties(tempAsset.getAudioVideoProperties().toBuilder()
//                                .videoDuration(501L)
//                                .build())
//                        .build()
//        ));
//
//        // Asset 5 should not be stitiched in as either missing AdAlias or Locations
//        hLSsableAdAssets.put("5", ImmutableList.of(
//                tempAsset.toBuilder()
//                        .audioVideoProperties(tempAsset.getAudioVideoProperties().toBuilder()
//                                .adAlias(null)
//                                .build())
//                        .build(),
//                tempAsset.toBuilder()
//                        .locations(ImmutableList.of())
//                        .build()
//        ));
//
//        return hLSsableAdAssets;
//    }
//
//    private Map<String, List<Asset>> createDASHAdsAssets(){
//        // Ad assets
//        Map<String, List<Asset>> hLSsableAdAssets = new HashMap<>();
//        Asset tempAsset = AssetTemplates.emptyBuilder()
//                .audioVideoProperties(AssetTemplates.defaultAvBuilder()
//                        .interstitialIds(ImmutableList.of("dash_main_1", "dash_main_2"))
//                        .videoDuration(100L)
//                        .adAlias(333L)
//                        .bitrate(500)
//                        .streamingTechnology(StreamingTechnology.DASH)
//                        .videoDuration(50L)
//                        .build())
//                .locations(AssetTemplates.defaultLocations("http://hostname/resource/3683/e894/53fe/4034-8698-758f062ffec4/855ae60a-57e0-436b-9545-cf9a22fb4836.mpd"))
//                .build();
//
//        // Ad Asset 1 should be stitched: InterstitialId match; Replication location provided
//        hLSsableAdAssets.put("1", ImmutableList.of(
//                tempAsset.toBuilder()
//                        .audioVideoProperties(tempAsset.getAudioVideoProperties().toBuilder()
//                                .ssaiReplicaLocation("interstitial/0/1374/manifest.mpd")
//                                .build())
//                        .build()
//        ));
//
//        // Asset 2 should not be stitiched: No replication location
//        hLSsableAdAssets.put("2", ImmutableList.of(
//                tempAsset.toBuilder()
//                        .audioVideoProperties(tempAsset.getAudioVideoProperties().toBuilder()
//                                .videoQuality(VideoQuality.HD)
//                                .build())
//                        .build(),
//                tempAsset.toBuilder()
//                        .audioVideoProperties(tempAsset.getAudioVideoProperties().toBuilder()
//                                .videoQuality(VideoQuality.UHD)
//                                .build())
//                        .build()
//        ));
//
//        // Asset 3 should be stitiched
//        hLSsableAdAssets.put("3", ImmutableList.of(
//                tempAsset.toBuilder()
//                        .audioVideoProperties(tempAsset.getAudioVideoProperties().toBuilder()
//                                .ssaiReplicaLocation("interstitial/31/1374/manifest.mpd")
//                                .build())
//                        .build(),
//                tempAsset.toBuilder()
//                        .audioVideoProperties(tempAsset.getAudioVideoProperties().toBuilder()
//                                .ssaiReplicaLocation("interstitial/32/1374/manifest.mpd")
//                                .build())
//                        .build()
//        ));
//
//        // Asset 4 should not be stitiched in as missing videoDuration or videoDuration longer than main content
//        hLSsableAdAssets.put("4", ImmutableList.of(
//                tempAsset.toBuilder()
//                        .audioVideoProperties(tempAsset.getAudioVideoProperties().toBuilder()
//                                .videoDuration(null)
//                                .build())
//                        .build(),
//                tempAsset.toBuilder()
//                        .audioVideoProperties(tempAsset.getAudioVideoProperties().toBuilder()
//                                .videoDuration(501L)
//                                .build())
//                        .build()
//        ));
//
//        // Asset 5 should not be stitiched in as either missing AdAlias or Locations
//        hLSsableAdAssets.put("5", ImmutableList.of(
//                tempAsset.toBuilder()
//                        .audioVideoProperties(tempAsset.getAudioVideoProperties().toBuilder()
//                                .adAlias(null)
//                                .build())
//                        .build(),
//                tempAsset.toBuilder()
//                        .locations(ImmutableList.of())
//                        .build()
//        ));
//
//        return hLSsableAdAssets;
//    }
//
//    private Map<String, List<PlaylistComponent>> createCuepoints() {
//        Map<String, List<PlaylistComponent>> cuepoints = new HashMap<>();
//        //Stitch
//        cuepoints.put("0",
//                Arrays.asList(PlaylistComponent.builder().withLinearAssets(
//                        Arrays.asList(LinearAsset.builder().withPlaybackId("1").withSequence(1).build())
//                ).withSequence(1).build()));
//        //No Stitch
//        cuepoints.put("99",
//                Arrays.asList(PlaylistComponent.builder().withLinearAssets(
//                        Arrays.asList(LinearAsset.builder().withPlaybackId("2").withSequence(2).build())
//                ).withSequence(2).build()));
//        //Stitch
//        cuepoints.put("199",
//                Arrays.asList(PlaylistComponent.builder().withLinearAssets(
//                        Arrays.asList(LinearAsset.builder().withPlaybackId("3").withSequence(3).build())
//                ).withSequence(3).build()));
//        //No Stitch
//        cuepoints.put("299",
//                Arrays.asList(PlaylistComponent.builder().withLinearAssets(
//                        Arrays.asList(LinearAsset.builder().withPlaybackId("4").withSequence(4).build())
//                ).withSequence(4).build()));
//        //No Stitch
//        cuepoints.put("399",
//                Arrays.asList(PlaylistComponent.builder().withLinearAssets(
//                        Arrays.asList(LinearAsset.builder().withPlaybackId("5").withSequence(5).build())
//                ).withSequence(5).build()));
//        return cuepoints;
//    }
//
//    private RequestContext createRequestContext(Map<String, List<PlaylistComponent>> cuepoints) {
//        return new RequestContext(MaterialType.Full, new com.amazon.urlvending.Asset(), new Device(), new Customer(),
//                Cdn.akamai.name(), encryptedMarketplaceId, null, null, null,
//                false, true, cuepoints);
//    }
//
//    private StitchedManifestUrlsResponse createStitchResults() {
//        return StitchedManifestUrlsResponse.builder()
//                .withStitchedManifestUrlMap(
//                        ImmutableMap.of(guids.get(0),urls.get(1).getUrlString(),
//                                guids.get(1),urls.get(3).getUrlString())
//                )
//                .withStitchingStatusMap(createAdsStitchingResults())
//                .build();
//    }
//
//    private StitchedManifestUrlsResponse createDASHStitchResults() {
//        return StitchedManifestUrlsResponse.builder()
//                .withStitchedManifestUrlMap(
//                        ImmutableMap.of(DASH_GUIDs.get(0),urls.get(1).getUrlString(),
//                                DASH_GUIDs.get(1),urls.get(3).getUrlString())
//                )
//                .withStitchingStatusMap(createDASHAdsStitchingResults())
//                .build();
//    }
//
//    private Map<String, List<com.amazon.atvmanifeststitchingservice.PlaylistComponent>> createAdsStitchingResults() {
//        Map<String, List<com.amazon.atvmanifeststitchingservice.PlaylistComponent>> rtnMap = new HashMap<>();
//        rtnMap.put("0", Arrays.asList(com.amazon.atvmanifeststitchingservice.PlaylistComponent
//                .builder()
//                .withSequenceNo(1)
//                .withLinearAssetList(Arrays.asList(
//                        com.amazon.atvmanifeststitchingservice.LinearAsset.builder()
//                                .withSequenceNo(1)
//                                .withStitched(true)
//                                .withDurationInSeconds(sableAdAssets.get("1").get(0).getAudioVideoProperties().getVideoDuration())
//                                .withAdAlias(sableAdAssets.get("1").get(0).getAudioVideoProperties().getAdAlias())
//                                .build()))
//                .build()));
//
//        rtnMap.put("199", Arrays.asList(com.amazon.atvmanifeststitchingservice.PlaylistComponent
//                .builder()
//                .withSequenceNo(3)
//                .withLinearAssetList(Arrays.asList(
//                        com.amazon.atvmanifeststitchingservice.LinearAsset.builder()
//                                .withSequenceNo(3)
//                                .withStitched(true)
//                                .withDurationInSeconds(sableAdAssets.get("3").get(0).getAudioVideoProperties().getVideoDuration())
//                                .withAdAlias(sableAdAssets.get("3").get(0).getAudioVideoProperties().getAdAlias())
//                                .build()))
//                .build()));
//        return rtnMap;
//    }
//
//    private Map<String, List<com.amazon.atvmanifeststitchingservice.PlaylistComponent>> createDASHAdsStitchingResults() {
//        Map<String, List<com.amazon.atvmanifeststitchingservice.PlaylistComponent>> rtnMap = new HashMap<>();
//        rtnMap.put("0", Arrays.asList(com.amazon.atvmanifeststitchingservice.PlaylistComponent
//                .builder()
//                .withSequenceNo(1)
//                .withLinearAssetList(Arrays.asList(
//                        com.amazon.atvmanifeststitchingservice.LinearAsset.builder()
//                                .withSequenceNo(1)
//                                .withStitched(true)
//                                .withDurationInSeconds(sableAdAssets.get("1").get(0).getAudioVideoProperties().getVideoDuration())
//                                .withAdAlias(sableAdAssets.get("1").get(0).getAudioVideoProperties().getAdAlias())
//                                .build()))
//                .build()));
//
//        rtnMap.put("199", Arrays.asList(com.amazon.atvmanifeststitchingservice.PlaylistComponent
//                .builder()
//                .withSequenceNo(3)
//                .withLinearAssetList(Arrays.asList(
//                        com.amazon.atvmanifeststitchingservice.LinearAsset.builder()
//                                .withSequenceNo(3)
//                                .withStitched(true)
//                                .withDurationInSeconds(sableAdAssets.get("3").get(0).getAudioVideoProperties().getVideoDuration())
//                                .withAdAlias(sableAdAssets.get("3").get(0).getAudioVideoProperties().getAdAlias())
//                                .build()))
//                .build()));
//        return rtnMap;
//    }
//}
