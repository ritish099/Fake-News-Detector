import pandas as pd
import os

path = '/Users/tithighosh/Library/Application Support/AirDroid/Downloads/article_data 2'
data=pd.read_csv('data.csv', index_col=0)

for lab, row in data.iterrows():
    
    if not str(data.loc[lab, 'Text'])=='nan':
        continue
    
    file = data.loc[lab, 'id']+'.txt' 
     
    with open(os.path.join(path, file)) as f:
        content = f.read()
        
    data.loc[lab, 'Text'] = content

data.drop(['filename', 'id'], axis=1, inplace = True) 
data.to_csv('fake_news_dataset.csv')
