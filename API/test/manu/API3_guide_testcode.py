from manufacturing.API3_guide.run import inference
import pandas as pd

# routes = oral
test_data = [[{'routes' : 'oral','formulation' : 'Capsule, Oral Capsule','method' : '직타','level' : ['사과', '혼합', '과립', '건조', '정립', '활택']}]]

# routes = parenteral
# test_data =  [[{'routes' : 'parenteral','formulation' : 'Injection','method' : '액상제조법','level' : ['사과', '조제(용해)', '무균 여과'],'cqas' : ['성상', '용출시험', '확인시험']}]]

# routes = local
# test_data = [[{'routes' : 'local','formulation' : 'Ophthalmic Solution','method' : '에멀전제조법','level' : ['사과', '조제(용해)'],'cqas' : ['성상', '용출시험', '확인시험']}]]

# 엔티시스 INPUT 값
# test_data = [[{'routes':'parenteral','formulation':'Ophthalmic Solution','method':'동결건조제조법','level':['사과','조제(용해)','무균 여과','충전','동결','1차건조','2차건조','밀봉'],'cqas':['appearance','identification','light scattering coefficient','pH','sterility test']}]]

# parameter
df = pd.DataFrame(test_data)
params = {}
batch_id = 1

# call inference
res = inference(df, params, batch_id)
print(res)