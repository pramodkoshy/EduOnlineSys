import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EduOnlineTestModule } from '../../../test.module';
import { QuestionItemDetailComponent } from 'app/entities/question-item/question-item-detail.component';
import { QuestionItem } from 'app/shared/model/question-item.model';

describe('Component Tests', () => {
  describe('QuestionItem Management Detail Component', () => {
    let comp: QuestionItemDetailComponent;
    let fixture: ComponentFixture<QuestionItemDetailComponent>;
    const route = ({ data: of({ questionItem: new QuestionItem(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EduOnlineTestModule],
        declarations: [QuestionItemDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(QuestionItemDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(QuestionItemDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load questionItem on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.questionItem).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
