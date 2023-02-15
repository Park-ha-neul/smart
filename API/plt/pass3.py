def process_for_train(pm):
    """학습용 데이터 전처리
    pm 함수
    load_data(use_cols) : 원본 데이터를 DataFrame의 형태로 로딩. use_cols를 이용하여 특정 컬럼들만 지정할 수 있음.
    save_data(df, index=True) : 전처리 완료된 데이터를 지정된 위치에 저장.
    get_ids() : id 목록 조회
    get_ids_by_class(label) : label값을 class로 갖는 id 목록 조회
    get_y(ids) : id 목록에 해당하는 label 값 조회

    pm 속성
    rule : 룰 정보 (dictionary)
    y_column : label 컬럼명
    y_value : label 컬럼의 값목록
    id_column : id 컬럼명
    meta_path : 전처리 모듈 및 메타 데이터를 저장하기 위한 경로
    source_path : 데이터 원본 경로
    target_path : 전처리 완료 데이터 경로
    module_path : 실제 전처리 물리 모듈 경로
    """
    mode = 'train'
    _do_preprocess(pm, mode)


def process_for_test(pm):
    """테스트용 데이터 전처리
    """
    mode = 'test'
    _do_preprocess(pm, mode)


def init_svc(im, rule):
    """추론 서비스 초기화
    """
    pass


def transform(df, params, batch_id):
    """추론을 위한 데이터 변환
    """
    return df


def _do_preprocess(pm, mode):
    pass

