INSERT  INTO
	lh_exam
      ( 
      ID                            
      ,EXAM_NAME                      
      ,QUESTION_COLUMN                    
      ,SCORE                          
      ,QUESTION_NUMBER                
      ,EXAM_TYPE                      
      ,ANSWER_TIME                    
      ,NUMBER          
      ,assign
      ,CREATE_BY                      
      ,CREATE_DATE                    
      ,REMARK                         
      ,RIGHT_ANSWER                   
      ) 
values
      (
      :lhExam.id                            
      ,:lhExam.examName                      
      ,:lhExam.questionColumn                    
      ,:lhExam.score                         
      ,:lhExam.questionNumber                
      ,:lhExam.examType                      
      ,:lhExam.answerTime                    
      ,:lhExam.number     
      ,:lhExam.assign
      ,:lhExam.createBy                      
      ,:lhExam.createDate                    
      ,:lhExam.remark                        
      ,:lhExam.rightAnswer                   
      )