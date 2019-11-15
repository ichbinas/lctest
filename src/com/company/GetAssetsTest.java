//package com.amazon.urlvending;
//
//import amazon.odin.MaterialType;
//import amazon.platform.config.AppConfig;
//import com.amazon.atv.cache.CacheClient;
//import com.amazon.atv.cache.CacheClientInMemory;
//import com.amazon.atv.encodedassetsprovider.AssetsProvider;
//import com.amazon.atv.encodedassetsprovider.V1AssetGroupKey;
//import com.amazon.atv.encodedassetsprovider.exception.AssetsProviderException;
//import com.amazon.atv.encodedasseturltypes.Asset;
//import com.amazon.atv.rtconfig.RuntimeConfigException;
//import com.amazon.atv.rtconfig.RuntimeConfigReference;
//import com.amazon.atv.rtconfig.RuntimeConfigReferenceFactory;
//import com.amazon.atv.rtconfig.appconfig.AppConfigRuntimeConfigReferenceFactory;
//import com.amazon.atv.rtconfig.serial.XStreamSerializer;
//import com.amazon.atv.urltransformation.utils.HostnameShardingPopularityHelper;
//import com.amazon.atvcachingcommon.caching.DataCache;
//import com.amazon.atvcachingcommon.store.DataStore;
//import com.amazon.atvcachingcommon.store.DataStoreException;
//import com.amazon.atvmanifestconfigservice.ATVManifestConfigServiceClient;
//import com.amazon.atvmanifestconfigservice.GetCacheKeyBatchRequest;
//import com.amazon.atvmanifestconfigservice.GetCacheKeyBatchResponse;
//import com.amazon.atvmanifestconfigservice.impl.GetCacheKeyBatchCall;
//import com.amazon.atvmanifestconfigservice.impl.GetCacheKeyCall;
//import com.amazon.atvmanifestconfigservice.types.AttributeKey;
//import com.amazon.atvmanifeststitchingservice.ATVManifestStitchingServiceClient;
//import com.amazon.atvmanifeststitchingservice.AdContext;
//import com.amazon.atvmanifeststitchingservice.GetStitchedManifestRequest;
//import com.amazon.atvmanifeststitchingservice.GetStitchedManifestUrlsRequest;
//import com.amazon.atvmanifeststitchingservice.GetStitchedManifestUrlsResponse;
//import com.amazon.atvmanifeststitchingservice.StitchedManifestUrlsResponse;
//import com.amazon.atvmanifeststitchingservice.impl.GetStitchedManifestCall;
//import com.amazon.atvmanifeststitchingservice.impl.GetStitchedManifestUrlsCall;
//import com.amazon.atvpolicyevaluationservice.ATVPolicyEvaluationServiceClient;
//import com.amazon.atvpolicyevaluationservice.CdnElement;
//import com.amazon.atvpolicyevaluationservice.EvaluateCdnPoliciesRequest;
//import com.amazon.atvpolicyevaluationservice.EvaluateCdnPoliciesResponse;
//import com.amazon.atvpolicyevaluationservice.impl.DeletePolicyCall;
//import com.amazon.atvpolicyevaluationservice.impl.EvaluateCdnPoliciesCall;
//import com.amazon.atvpolicyevaluationservice.impl.GetPolicyCall;
//import com.amazon.atvpolicyevaluationservice.impl.ListPoliciesCall;
//import com.amazon.atvpolicyevaluationservice.impl.PutPolicyCall;
//import com.amazon.atvpolicyevaluationservice.impl.TracePoliciesCall;
//import com.amazon.coral.metrics.MetricsFactory;
//import com.amazon.encoding.assets.model.external.AssetGroup;
//import com.amazon.encoding.assets.model.holdback.Holdbacks;
//import com.amazon.jacksonion.JoiObjectMapper;
//import com.amazon.metrics.declarative.DefaultMetricsManager;
//import com.amazon.metrics.declarative.MetricsManager;
//import com.amazon.urlvending.activity.GetAssetsActivity;
//import com.amazon.urlvending.activity.GetUrlsV2Activity;
//import com.amazon.urlvending.config.MemCacheConfig;
//import com.amazon.urlvending.config.ServicesConfig;
//import com.amazon.urlvending.credentials.MaterialStore;
//import com.amazon.urlvending.credentials.MemoryMaterialStore;
//import com.amazon.urlvending.guidvending.serviceclients.GUIDProvider;
//import com.amazon.urlvending.guidvending.serviceclients.SimpleGUIDProvider;
//import com.amazon.urlvending.metrics.MetricsContext;
//import com.amazon.urlvending.overrides.CustomerOverridesDynamoDB;
//import com.amazon.urlvending.overrides.CustomerOverridesHelper;
//import com.amazon.urlvending.types.FulfillmentData;
//import com.amazon.urlvending.utils.CounterMetricsFactory;
//import com.amazon.urlvending.utils.UrlSetRankingHelper;
//import com.amazon.urlvending.utils.UrlSignerTestConstants;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
//import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
//import com.amazonaws.services.dynamodbv2.model.ScanRequest;
//import com.amazonaws.services.dynamodbv2.model.ScanResult;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.google.common.collect.ImmutableList;
//import lombok.Data;
//import org.apache.commons.beanutils.BeanUtils;
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.io.IOUtils;
//import org.apache.commons.lang.StringUtils;
//import org.easymock.EasyMock;
//import org.junit.Assert;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.invocation.InvocationOnMock;
//import org.mockito.stubbing.Answer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.support.AnnotationConfigContextLoader;
//
//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.NoSuchAlgorithmException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//import static com.amazon.urlvending.UrlSetsABValidator.clash;
//import static com.amazon.urlvending.utils.CuepointHelper.toAdUrlsPropertiesMap;
//import static org.mockito.Matchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
///**
// * This unit test runs the whole GetAssetsActivity from end to end, and verifies that
// * GetAssetsActivity response is equivalent to expected
// *
// * @author xuzhi
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {
//        GetAssetsSysABTest.additionalSprintConfig.class})
//public class GetAssetsSysABTest {
//    public static final EvaluateCdnPoliciesCall mockEvaluateCdnPoliciesCall = EasyMock.createMock(EvaluateCdnPoliciesCall.class);
//    public static final GetCacheKeyBatchCall mockGetCacheKeyBatchCall = EasyMock.createMock(GetCacheKeyBatchCall.class);
//    public static final DynamoDBMapper mockDynamoDBMapper = EasyMock.createMock(DynamoDBMapper.class);
//    public static final GetStitchedManifestUrlsCall mockGetStitchedManifestUrlsCall = mock(GetStitchedManifestUrlsCall.class);
//
//    public static EvaluateCdnPoliciesResponse pesResponse;
//    public static final TypeReference<List<CdnElement>> CDN_ELEMENT_LIST_TYPE_REF = new TypeReference<List<CdnElement>>() {
//    };
//    public static Map<String, AssetGroup> assetGroups;
//    public static List<GetAssetsRequestResponse> requestResponse;
//    public static CacheClientInMemory<String, FulfillmentData> fulfillmentCache = new CacheClientInMemory<>();
//
//    public static final JoiObjectMapper OM = new JoiObjectMapper();
//    public static final XStreamSerializer RT_SERIALIZER = new XStreamSerializer();
//
//    //For SSAI, need a special UUID generator to get predetermined UUID specified in GetAssets Request&Response bodies.
//    private enum UUIDFLAG {SSAIHLS, SSAIDASH, SSAISS};
//
//    public static UUIDFLAG uuidFlag;
//
//    public static final String PES_RESPONSE_JSON = "[\n" +
//            "  {\n" +
//            "    \"name\": \"akamai\",\n" +
//            "    \"weight\": 0.1,\n" +
//            "    \"origins\": [\n" +
//            "      \"agile\",\n" +
//            "      \"akamai\",\n" +
//            "      \"cloudfront\",\n" +
//            "      \"level3\",\n" +
//            "      \"limelight\",\n" +
//            "      \"netstorage\",\n" +
//            "      \"osp\",\n" +
//            "      \"s3\",\n" +
//            "      \"ssai_s3\",\n" +
//            "      \"s3_dub\",\n" +
//            "      \"s3_gru\",\n" +
//            "      \"s3_iad\",\n" +
//            "      \"s3_nrt\",\n" +
//            "      \"s3_sin\",\n" +
//            "      \"s3_syd\"\n" +
//            "    ]\n" +
//            "  },\n" +
//            "  {\n" +
//            "    \"name\": \"cloudfront\",\n" +
//            "    \"weight\": 0.4,\n" +
//            "    \"origins\": [\n" +
//            "      \"agile\",\n" +
//            "      \"akamai\",\n" +
//            "      \"cloudfront\",\n" +
//            "      \"level3\",\n" +
//            "      \"limelight\",\n" +
//            "      \"netstorage\",\n" +
//            "      \"osp\",\n" +
//            "      \"s3\",\n" +
//            "      \"ssai_s3\",\n" +
//            "      \"s3_dub\",\n" +
//            "      \"s3_gru\",\n" +
//            "      \"s3_iad\",\n" +
//            "      \"s3_nrt\",\n" +
//            "      \"s3_sin\",\n" +
//            "      \"s3_syd\"\n" +
//            "    ]\n" +
//            "  },\n" +
//            "  {\n" +
//            "    \"name\": \"level3\",\n" +
//            "    \"weight\": 0.2,\n" +
//            "    \"origins\": [\n" +
//            "      \"agile\",\n" +
//            "      \"akamai\",\n" +
//            "      \"cloudfront\",\n" +
//            "      \"level3\",\n" +
//            "      \"limelight\",\n" +
//            "      \"netstorage\",\n" +
//            "      \"osp\",\n" +
//            "      \"s3\",\n" +
//            "      \"ssai_s3\",\n" +
//            "      \"s3_dub\",\n" +
//            "      \"s3_gru\",\n" +
//            "      \"s3_iad\",\n" +
//            "      \"s3_nrt\",\n" +
//            "      \"s3_sin\",\n" +
//            "      \"s3_syd\"\n" +
//            "    ]\n" +
//            "  },\n" +
//            "  {\n" +
//            "    \"name\": \"limelight\",\n" +
//            "    \"weight\": 0.3,\n" +
//            "    \"origins\": [\n" +
//            "      \"agile\",\n" +
//            "      \"akamai\",\n" +
//            "      \"cloudfront\",\n" +
//            "      \"level3\",\n" +
//            "      \"limelight\",\n" +
//            "      \"netstorage\",\n" +
//            "      \"osp\",\n" +
//            "      \"s3\",\n" +
//            "      \"ssai_s3\",\n" +
//            "      \"s3_dub\",\n" +
//            "      \"s3_gru\",\n" +
//            "      \"s3_iad\",\n" +
//            "      \"s3_nrt\",\n" +
//            "      \"s3_sin\",\n" +
//            "      \"s3_syd\"\n" +
//            "    ]\n" +
//            "  }\n" +
//            "]";
//
//    static {
//        // for testing purpose only, fail on unknown field to avoid bad data being ignored
//        OM.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
//        try {
//            pesResponse = EvaluateCdnPoliciesResponse.builder().withResult(OM.readValue(PES_RESPONSE_JSON, CDN_ELEMENT_LIST_TYPE_REF)).build();
//        } catch (IOException e) {
//            Assert.fail(e.getMessage());
//        }
//    }
//
//    public static Map<Class<?>, Object> RT_CONF_MAP = new ConcurrentHashMap<>();
//
//    @Configuration
//    @Import({ServicesConfig.class, MemCacheConfig.class})
//    static class additionalSprintConfig {
//        @Bean
//        public static PropertySourcesPlaceholderConfigurer propertiesResolver() {
//            return new PropertySourcesPlaceholderConfigurer();
//        }
//
//        @Bean
//        public RuntimeConfigReferenceFactory runtimeConfigReferenceFactory() {
//            return new AppConfigRuntimeConfigReferenceFactory() {
//                @Override
//                @SuppressWarnings("unchecked")
//                public <T> RuntimeConfigReference<T> getConfigReference(final String name, final Class<T> clazz)
//                        throws RuntimeConfigException {
//                    return () -> {
//                        T rtConf = (T) RT_CONF_MAP.get(clazz);
//                        if (null == rtConf) {
//                            try (FileInputStream in = new FileInputStream(new File("var/state/rt_config_cache/" + AppConfig.getRealm().name(), name + ".state.xml"))) {
//                                rtConf = clazz.newInstance();
//                                T stored = (T) RT_SERIALIZER.deserialize(IOUtils.toByteArray(in));
//                                for (Field field : stored.getClass().getDeclaredFields()) {
//                                    field.setAccessible(true);
//                                    try {
//                                        BeanUtils.copyProperty(rtConf, field.getName(), field.get(stored));
//                                    } catch (Exception e) {
//                                        // it's expected that a lot of fields, like static final ones, cannot be set
//                                    }
//                                }
//                                RT_CONF_MAP.put(clazz, rtConf);
//                            } catch (Exception e) {
//                                Assert.fail(e.getMessage());
//                            }
//                        }
//                        return rtConf;
//                    };
//                }
//            };
//        }
//
//        @Bean
//        public HostnameShardingPopularityHelper popularityHelper() {
//            final MetricsManager metricsManager = new DefaultMetricsManager(metricsFactory());
//            final DataStore<Map<String, Float>> dataStore = new DataStore<Map<String, Float>>() {
//                @Nonnull
//                @Override
//                public ImmutableList<Metadata> listItems() throws DataStoreException {
//                    throw new IllegalStateException("Unexcepted method call");
//                }
//
//                @Override
//                public void put(@Nonnull String name, @Nonnull Map<String, Float> item) throws DataStoreException {
//                    throw new IllegalStateException("Unexcepted method call");
//                }
//
//                @Nullable
//                @Override
//                public ItemWithMetadata<Map<String, Float>> get(@Nonnull String name) throws DataStoreException {
//                    throw new IllegalStateException("Unexcepted method call");
//                }
//
//                @Override
//                public void delete(@Nonnull String name) throws DataStoreException {
//                    throw new IllegalStateException("Unexcepted method call");
//                }
//            };
//            final DataCache<Map<String, Float>> dataCache = new DataCache<>(dataStore, metricsManager);
//
//
//            return new HostnameShardingPopularityHelper(dataCache);
//        }
//
//        @Bean
//        public MetricsFactory metricsFactory() {
//            return new CounterMetricsFactory();
//        }
//
//        @Bean
//        public GetUrlsV2Activity activity() {
//            return new GetUrlsV2Activity();
//        }
//
//        @Bean
//        public GetAssetsActivity getAssetsActivity() {
//            return new GetAssetsActivity();
//        }
//
//        @Bean
//        public UrlSetRankingHelper urlSetRankingHelper() {
//            return new UrlSetRankingHelper();
//        }
//
//        @Bean
//        public CustomerOverridesHelper customerOverridesHelper() {
//            return new CustomerOverridesHelper();
//        }
//
//        @Bean
//        @Qualifier("urlVendingApplication")
//        public com.amazon.urlvending.UrlVendingApplication urlVendingApplication() {
//            return new com.amazon.urlvending.UrlVendingApplication();
//        }
//
//        @Bean
//        @Qualifier("guidProvider")
//        public GUIDProvider simpleGUIDProvider() {
//            return mock(SimpleGUIDProvider.class);
//        }
//
//        @Bean
//        public AssetsProvider assetsProvider() {
//            return new AssetsProvider() {
//                @Override
//                public Holdbacks provideHoldbacks(String pvid, String policyId) throws AssetsProviderException {
//                    return null;
//                }
//
//                @Override
//                public AssetGroup provideAssetGroup(String encryptedMarketplaceId, String titleId, String assetGroupType) throws AssetsProviderException {
//                    return assetGroups.get(encodedAssetsV1Key(titleId, assetGroupType));
//                }
//
//                @Override
//                public com.amazon.encoding.assets.model.v2.AssetGroup provideV2AssetGroup(String encryptedMarketplaceId, String titleId, String assetType, String streamingTech) throws AssetsProviderException {
//                    throw new IllegalStateException("Unexcepted method call: provideAssetGroup");
//                }
//
//                @Override
//                public List<AssetGroup> provideAssetGroups(String encryptedMarketplaceId, String titleId, List<String> assetGroupTypes) throws AssetsProviderException {
//                    return assetGroupTypes.stream()
//                            .map(assetGroupType -> assetGroups.get(encodedAssetsV1Key(titleId, assetGroupType)))
//                            .collect(Collectors.toList());
//                }
//
//                @Override
//                public Map<V1AssetGroupKey, AssetGroup> provideAssetGroups(String encryptedMarketplaceId, Collection<V1AssetGroupKey> keys) throws AssetsProviderException {
//                    throw new IllegalStateException("Unexcepted method call: provideAssetGroups");
//                }
//
//                @Nonnull
//                @Override
//                public List<Asset> provideAssets(@Nonnull String pid) throws AssetsProviderException {
//                    throw new IllegalStateException("Unexcepted method call: provideAssets");
//                }
//            };
//        }
//
//        @Bean
//        @Qualifier("policyEvaluationServiceClient")
//        public ATVPolicyEvaluationServiceClient policyEvaluationServiceClient() {
//            return new ATVPolicyEvaluationServiceClient() {
//                @Override
//                public DeletePolicyCall newDeletePolicyCall() {
//                    throw new IllegalStateException("Unexcepted method call");
//                }
//
//                @Override
//                public TracePoliciesCall newTracePoliciesCall() {
//                    throw new IllegalStateException("Unexcepted method call");
//                }
//
//                @Override
//                public PutPolicyCall newPutPolicyCall() {
//                    throw new IllegalStateException("Unexcepted method call");
//                }
//
//                @Override
//                public GetPolicyCall newGetPolicyCall() {
//                    throw new IllegalStateException("Unexcepted method call");
//                }
//
//                @Override
//                public EvaluateCdnPoliciesCall newEvaluateCdnPoliciesCall() {
//                    return mockEvaluateCdnPoliciesCall;
//                }
//
//                @Override
//                public ListPoliciesCall newListPoliciesCall() {
//                    throw new IllegalStateException("Unexcepted method call");
//                }
//            };
//        }
//
//        @Bean
//        public DynamoDBMapper dynamoDBMapper() {
//            return mockDynamoDBMapper;
//        }
//
//        @Bean
//        @Qualifier("aTVManifestConfigServiceClient")
//        public ATVManifestConfigServiceClient manifestConfigServiceClient() {
//            return new ATVManifestConfigServiceClient() {
//                @Override
//                public GetCacheKeyBatchCall newGetCacheKeyBatchCall() {
//                    return mockGetCacheKeyBatchCall;
//                }
//
//                @Override
//                public GetCacheKeyCall newGetCacheKeyCall() {
//                    throw new IllegalStateException("Unexcepted method call");
//                }
//            };
//        }
//
//        @Bean
//        @Qualifier("atvManifestStitchingServiceClient")
//        public ATVManifestStitchingServiceClient atvManifestStitchingServiceClient() {
//            return new ATVManifestStitchingServiceClient() {
//                @Override
//                public GetStitchedManifestUrlsCall newGetStitchedManifestUrlsCall() {
//                    return mockGetStitchedManifestUrlsCall;
//                }
//
//                @Override
//                public GetStitchedManifestCall newGetStitchedManifestCall() {
//                    throw new IllegalStateException("Unexpected method call to newGetStitchedManifestCall");
//                }
//            };
//        }
//
//        @Bean
//        public MaterialStore createCachedMaterialStore() throws NoSuchAlgorithmException {
//            MemoryMaterialStore memoryMaterialStore = new MemoryMaterialStore();
//            memoryMaterialStore.setMaterial(UrlSignerTestConstants.DEFAULT_AK_HTTP_HASH_SECRET_MATERIAL_NAME, MaterialType.Credential, "baadbaadbaad".getBytes());
//            memoryMaterialStore.setMaterial(AppConfig.findString("UrlSigning.Limelight.Http.secretMaterialName"), MaterialType.Credential, "dkf94lSk3kdl20kd2dkcaa".getBytes());
//            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
//            kpg.initialize(2048);
//            KeyPair keys = kpg.generateKeyPair();
//            memoryMaterialStore.setMaterial(AppConfig.findString("UrlSigning.Cloudfront.Http.privateSecretMaterialName"), MaterialType.PrivateKey, keys.getPrivate().getEncoded());
//            memoryMaterialStore.setMaterial(AppConfig.findString("UrlSigning.Cloudfront.Http.keyPairSecretMaterialName"), MaterialType.Credential, UrlSignerTestConstants.DEFAULT_CLOUDFRONT_KEY_PAIR_ID.getBytes());
//            memoryMaterialStore.setMaterial(AppConfig.findString("UrlSigning.Level3.Http.secretMaterialName"), MaterialType.Principal, "1".getBytes());
//            memoryMaterialStore.setMaterial(AppConfig.findString("UrlSigning.Level3.Http.secretMaterialName"), MaterialType.Credential, "abc".getBytes());
//
//            return memoryMaterialStore;
//        }
//
//        @Bean
//        public CacheClient<String, FulfillmentData> fulfillmentMemcacheClient() {
//            return fulfillmentCache;
//        }
//    }
//
//    @Autowired
//    private GetAssetsActivity getAssetsActivity;
//
//    @Autowired
//    private MetricsFactory metricsFactory;
//
//    @Autowired
//    GUIDProvider guidProvider;
//
//    @BeforeClass
//    public static void beforeClass() throws IOException {
//        System.setProperty("test.domain", "master");
//        System.setProperty("apollo.OCF.UrlVendingService.sableClientName", "HTTP");
//        System.setProperty("CORAL_CONFIG_PATH", "build/private/coral-config");
//        System.setProperty("root", "build/private");
//
//        AppConfigInitializer.initialize();
//        System.setProperty("runtimeConfigBackupPath", "var/state/rt_config_cache/" + AppConfig.getRealm().name());
//
//        assetGroups = loadAssetGroups();
//        requestResponse = loadRequestResponse();
//    }
//
//    @Test
//    public void testPlaybackUrlsFullVideo() throws IOException {
//        for (GetAssetsRequestResponse rr : requestResponse) {
//            //This is a SSAI HLS request
//            if (rr.getRequest().getCuepoints() != null && StringUtils.equals(rr.getRequest().getDevice().getDrmSupported(), "FAIRPLAY")) {
//                uuidFlag = UUIDFLAG.SSAIHLS;
//            } else if (rr.getRequest().getCuepoints() != null &&
//                    rr.getRequest().getAssetProperties().getCsaiAdUrlsProperties() == null &&
//                    rr.getRequest().getDevice().getStreamingTechnologies().contains("DASH")) {
//                uuidFlag = UUIDFLAG.SSAIDASH;
//            } else {
//                uuidFlag = null;
//            }
//
//            testGetAssets(rr.getRequest(), rr.getResponse());
//        }
//
//        System.out.println("A/B Tested " + requestResponse.size() + " GetAssets Requests. ");
//    }
//
//    private Map<String, String> getExpectedPlaybackUrlsCdnCacheKey(GetAssetsResponse response) {
//        return null == response.getPlaybackUrls() ? Collections.EMPTY_MAP : response.getPlaybackUrls().getUrlSets().values().stream()
//                .map(urlSet -> urlSet.getUrls().getManifest())
//                .collect(Collectors.toMap(manifestUrl -> manifestUrl.getCdn().toLowerCase(), manifestUrl -> getCacheKey(manifestUrl.getUrl())));
//    }
//
//    private String getCacheKey(String urlString) {
//        return StringUtils.substringBetween(urlString, "2$", "/");
//    }
//
//    private GetStitchedManifestUrlsResponse mockMSSResponse(GetStitchedManifestUrlsRequest request) {
//        AdContext adContext= request.getAdContext();
//
//        Map<String, List<com.amazon.atvmanifeststitchingservice.PlaylistComponent>> cuepoints = adContext.getCuepointMap();
//        Map<String, List<com.amazon.atvmanifeststitchingservice.PlaylistComponent>> stitchingStatusMap =
//                cuepoints.entrySet().stream()
//                        .collect(Collectors.toMap(c -> c.getKey(),
//                                c -> c.getValue().stream()
//                                        .map(p -> {
//                                            long playlistSeq = p.getSequenceNo();
//
//                                            return com.amazon.atvmanifeststitchingservice.PlaylistComponent.builder()
//                                                    .withSequenceNo(playlistSeq)
//                                                    .withLinearAssetList(
//                                                            p.getLinearAssetList().stream()
//                                                                    .map(l -> com.amazon.atvmanifeststitchingservice.LinearAsset.builder()
//                                                                            .withSequenceNo(l.getSequenceNo())
//                                                                            .withAdAlias(l.getAdAlias())
//                                                                            .withDurationInSeconds(l.getAdAlias())
//                                                                            .withDriftMetadata(l.getDriftMetadata())
//                                                                            .withReplicationLocation(l.getReplicationLocation())
//                                                                            .withAdManifestUrl(l.getAdManifestUrl())
//                                                                            .withStitched(true)
//                                                                            .build()
//                                                                    )
//                                                                    .collect(Collectors.toList())
//                                                    )
//                                                    .build();
//                                        })
//                                        .collect(Collectors.toList()),
//                                (c1, c2) -> c1));
//
//        Map<String, String> stitchedMainContentUrl = adContext.getFeatureManifestUrlMap();
//
//        StitchedManifestUrlsResponse response = StitchedManifestUrlsResponse.builder()
//                .withStitchedManifestUrlMap(stitchedMainContentUrl)
//                .withStitchingStatusMap(stitchingStatusMap)
//                .build();
//
//        return
//                GetStitchedManifestUrlsResponse.builder().withStitchedManifestUrlsResponse(response).build();
//    }
//
//    private void setMocks(Map<String, String> expectedCdnCacheKey) throws IOException {
//        fulfillmentCache.getInternalData().clear();
//        EasyMock.reset(mockEvaluateCdnPoliciesCall, mockDynamoDBMapper, mockGetCacheKeyBatchCall);
//
//        EasyMock.expect(mockEvaluateCdnPoliciesCall.addAttachment(EasyMock.anyObject()))
//                .andReturn(mockEvaluateCdnPoliciesCall);
//
//        EasyMock.expect(mockEvaluateCdnPoliciesCall.call(EasyMock.anyObject(EvaluateCdnPoliciesRequest.class)))
//                .andReturn(pesResponse);
//
//        mockEvaluateCdnPoliciesCall.setRequestId("test-request-id");
//        EasyMock.expectLastCall();
//
//        // expect call to Scan Customer Override 0 or 1 time, as the result can be cached.
//        EasyMock.expect(mockDynamoDBMapper.scan(EasyMock.eq(CustomerOverridesDynamoDB.class), EasyMock.anyObject())).andAnswer(
//                () -> new PaginatedScanList(mockDynamoDBMapper,
//                        (Class<CustomerOverridesDynamoDB>) EasyMock.getCurrentArguments()[0], null,
//                        new ScanRequest(), new ScanResult(), null, null)).times(0, 1);
//
//        if (!expectedCdnCacheKey.entrySet().stream().noneMatch(e -> StringUtils.isNotBlank(e.getValue()))) {
//            mockGetCacheKeyBatchCall.setRequestId("test-request-id");
//            EasyMock.expectLastCall();
//            EasyMock.expect(mockGetCacheKeyBatchCall.addAttachment(EasyMock.anyObject())).andReturn(mockGetCacheKeyBatchCall);
//            EasyMock.expect(mockGetCacheKeyBatchCall.call(EasyMock.anyObject(GetCacheKeyBatchRequest.class)))
//                    .andAnswer(() -> {
//                        GetCacheKeyBatchRequest req = (GetCacheKeyBatchRequest) EasyMock.getCurrentArguments()[0];
//                        return GetCacheKeyBatchResponse.builder()
//                                .withCacheKeys(IntStream.range(0, req.getObjects().size())
//                                        .boxed()
//                                        .collect(Collectors.toMap(i -> i.toString(),
//                                                i -> StringUtils.defaultString(
//                                                        expectedCdnCacheKey.get(req.getObjects().get(i).getAttributes()
//                                                                .get(AttributeKey.CDN)),
//                                                        "the CDN " + req.getObjects().get(i).getAttributes().get(AttributeKey.CDN)
//                                                                + " doesn't exist in expected response, so it doesn't matter what we return for the cacheKey. "),
//                                                (e1, e2) -> e1)))
//                                .build();
//                    });
//        }
//
//        GetStitchedManifestUrlsResponse mssResponse = OM.readValue("{\n" +
//                        "  \"stitchedManifestUrlsResponse\": {\n" +
//                        "    \"stitchedManifestUrlMap\": {\n" +
//                        "      \"uuid8\": \"http://13s3.lvlt.hls.us.aiv-cdn.net/d/2$Zw7qXC5ZFrueyWD4VFb9NrYM0X4~/1@ded7a9e4/prod/cda8/a515/70be/4fc3-8c77-b32719a36cab/caa081d2-7519-4c21-891b-e27ccd3d5eb0.m3u8\",\n" +
//                        "      \"uuid6\": \"http://a489avodhlss3us-a.akamaihd.net/d/2$Zw7qXC5ZFrueyWD4VFb9NrYM0X4~/1@ded7a9e4/ondemand/cda8/a515/70be/4fc3-8c77-b32719a36cab/caa081d2-7519-4c21-891b-e27ccd3d5eb0.m3u8\",\n" +
//                        "      \"uuid7\": \"http://s3.ll.hls.us.aiv-cdn.net/d/2$Zw7qXC5ZFrueyWD4VFb9NrYM0X4~/1@ded7a9e4/cda8/a515/70be/4fc3-8c77-b32719a36cab/caa081d2-7519-4c21-891b-e27ccd3d5eb0.m3u8\"\n" +
//                        "    },\n" +
//                        "    \"stitchingStatusMap\": {\n" +
//                        "      \"0\": [\n" +
//                        "        {\n" +
//                        "          \"linearAssetList\": [\n" +
//                        "            {\n" +
//                        "              \"adManifestUrl\": \"http://s3.amazonaws.com/aiv-prod-hls-fp/b66d/572c/2471/4b9f-9d8b-07cf120ce861/0fec6544-8c40-483c-b270-71ece3a1af33.m3u8\",\n" +
//                        "              \"adAlias\": 0,\n" +
//                        "              \"durationInSeconds\": 10,\n" +
//                        "              \"sequenceNo\": 0,\n" +
//                        "              \"stitched\": true,\n" +
//                        "              \"driftMetadata\": null\n" +
//                        "            }\n" +
//                        "          ],\n" +
//                        "          \"sequenceNo\": 1\n" +
//                        "        }\n" +
//                        "      ]\n" +
//                        "    }\n" +
//                        "  }\n" +
//                        "}",
//                com.amazon.atvmanifeststitchingservice.GetStitchedManifestUrlsResponse.class);
//        if(uuidFlag == UUIDFLAG.SSAIHLS) {
//            when(mockGetStitchedManifestUrlsCall.call(any(GetStitchedManifestUrlsRequest.class)))
//                    .thenReturn(mssResponse);
//        }
//
//        mockGetStitchedManifestUrlsCall.setRequestId("test-request-id");
//
//        EasyMock.replay(mockEvaluateCdnPoliciesCall, mockDynamoDBMapper, mockGetCacheKeyBatchCall);
//
//        when(guidProvider.getGUID()).thenAnswer(new Answer() {
//            private int count = 0;
//            private int hlsCount = 0;
//
//            public Object answer(InvocationOnMock invocation) {
//                if (uuidFlag == UUIDFLAG.SSAIHLS) {
//                    return "uuid" + (++hlsCount);
//                }
//                return "uuid" + (++count);
//            }
//        });
//
//        MetricsContext.setMetrics(metricsFactory.newMetrics());
//        MetricsContext.setRequestId("test-request-id");
//    }
//
//    private void verifyMocks() {
//        MetricsContext.getMetrics().close();
//        MetricsContext.clear();
//        CounterMetricsFactory.assertMetricsClosedProperly();
//        EasyMock.verify(mockEvaluateCdnPoliciesCall, mockDynamoDBMapper, mockGetCacheKeyBatchCall);
//    }
//
//    private void verifyClashes(GetAssetsRequest request, GetAssetsResponse expected, Object actual,
//                               List<String> clashReasons, List<String> clashMessages) throws JsonProcessingException {
//        if (CollectionUtils.isNotEmpty(clashReasons) || CollectionUtils.isNotEmpty(clashMessages)) {
//            clashReasons.forEach(System.out::println);
//            clashMessages.forEach(System.out::println);
//
//            System.out.println("Found clash for request " + OM.writeValueAsString(request));
//            System.out.println("Expected response: " + OM.writeValueAsString(expected));
//            System.out.println("Actual response " + OM.writeValueAsString(actual));
//
//            Assert.fail();
//        }
//    }
//
//    public FulfillmentData newFulfillmentReport(String cdn, Device device) {
//        // UVS reports requested DRM and Protocol to fulfillment. Not sure if this is desired.
//        // https://sim.amazon.com/issues/PI-2049
//        return new FulfillmentData(cdn, device.getDrmSupported(), device.getProtocol());
//    }
//
//    public Map<String, FulfillmentData> expectedFulfillmentReport(Device device, PlaybackUrls actual) {
//        return actual.getUrlSets().entrySet().stream()
//                .collect(Collectors.toMap(e -> "FULFILLMENT_" + e.getKey(),
//                        e -> newFulfillmentReport(e.getValue().getUrls().getManifest().getCdn().toLowerCase(), device),
//                        (e1, e2) -> e1));
//    }
//
//    public void verifyFulfillmentReport(Map<String, FulfillmentData> expected, Map<String, FulfillmentData> actual,
//                                        List<String> clashReasons, List<String> clashMessages) throws JsonProcessingException {
//        for (Map.Entry<String, FulfillmentData> expEntry : expected.entrySet()) {
//            FulfillmentData actFfd = actual.get(expEntry.getKey());
//            if (null == actFfd) {
//                clashReasons.add("Missing expected fulfillment report for " + expEntry.getKey());
//                clashMessages.add(clash(expEntry.getKey() + " fulfillment report = " + OM.writeValueAsString(expEntry.getValue()), null));
//            } else {
//                if (!StringUtils.equals(expEntry.getValue().getCdn(), actFfd.getCdn())
//                        || !StringUtils.equals(expEntry.getValue().getDrm(), actFfd.getDrm())
//                        || !StringUtils.equals(expEntry.getValue().getProtocol(), actFfd.getProtocol())) {
//                    clashReasons.add("Mismatch fulfillment report for " + expEntry.getKey());
//                    clashMessages.add(clash(expEntry.getKey() + " fulfillmentData = " + OM.writeValueAsString(expEntry.getValue()), OM.writeValueAsString(actFfd)));
//                }
//            }
//        }
//
//        for (Map.Entry<String, FulfillmentData> actEntry : actual.entrySet()) {
//            if (!expected.containsKey(actEntry.getKey())) {
//                clashReasons.add("Unexpected redundant fulfillment report for " + actEntry.getKey());
//                clashMessages.add(clash(actEntry.getKey() + " doesn't exist", OM.writeValueAsString(actEntry.getValue())));
//            }
//        }
//    }
//
//    public void testGetAssets(GetAssetsRequest request, GetAssetsResponse expected) throws IOException {
//        Map<String, String> expectedCdnCacheKey = getExpectedPlaybackUrlsCdnCacheKey(expected);
//        setMocks(expectedCdnCacheKey);
//
//        GetAssetsResponse actual = getAssetsActivity.getAssets(request);
//
////        String printResponse;
////        if(uuidFlag == UUIDFLAG.SSAIDASH) {
////            printResponse = OM.writeValueAsString(actual);
////        }
//
//        verifyMocks();
//
//        List<String> clashReasons = new ArrayList<>();
//        List<String> clashMessages = new ArrayList<>();
//        PlaybackUrlsABValidator.validateResponse(expected.getPlaybackUrls(), actual.getPlaybackUrls(), clashReasons, clashMessages);
//        CuepointsABValidator.validateResponse(expected.getAdUrls(), actual.getAdUrls(), clashReasons, clashMessages);
//        CuepointsABValidator.validateCuepoints(expected.getCuepoints(), actual.getCuepoints(), clashReasons, clashMessages);
//        CuepointsABValidator.validateResponse(expected.getAdUrls(), toAdUrlsPropertiesMap(actual.getCuepoints()), clashReasons, clashMessages);
//        if (null != expected.getPlaybackUrls()) {
//            verifyFulfillmentReport(fulfillmentCache.getInternalData(),
//                    expectedFulfillmentReport(request.getDevice(), actual.getPlaybackUrls()), clashReasons, clashMessages);
//        }
//        verifyClashes(request, expected, actual, clashReasons, clashMessages);
//    }
//
//    public static final TypeReference<List<AssetGroup>> ASSET_GROUP_LIST_TYPE_REF = new TypeReference<List<AssetGroup>>() {
//    };
//
//    public static Map<String, AssetGroup> loadAssetGroups() {
//        return FileUtils.listFiles(new File("tst-data/EncodedAssets"), null, true).stream()
//                .map(file -> {
//                    try {
//                        return (List<AssetGroup>) OM.readValue(file, ASSET_GROUP_LIST_TYPE_REF);:q
//
//                    } catch (IOException e) {
//                        System.out.println("Exception thrown when parsing file=" + file.getName());
//                        e.printStackTrace();
//                    }
//                    return null;
//                })
//                .filter(Objects::nonNull)
//                .flatMap(List::stream)
//                .collect(Collectors.toMap(
//                        ag -> encodedAssetsV1Key(ag.getTitleId(), ag.getAssetGroupType()),
//                        Function.identity(), (e1, e2) -> e1));
//    }
//
//    private static String encodedAssetsV1Key(
//            final String titleId,
//            final String assetGroupType) {
//        return titleId + ":" + assetGroupType;
//    }
//
//    @Data
//    public static class GetAssetsRequestResponse {
//        GetAssetsRequest request;
//        GetAssetsResponse response;
//    }
//
//    public static List<GetAssetsRequestResponse> loadRequestResponse() {
//        return FileUtils.listFiles(new File("tst-data/GetAssets"), null, true).stream()
//                .map(file -> {
//                    try {
//                        return OM.readValue(file, GetAssetsRequestResponse.class);
//                    } catch (IOException e) {
//                        System.out.println("Exception thrown when parsing file: " + file.getName());
//                        e.printStackTrace();
//                    }
//                    return null;
//                })
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//    }
//}
