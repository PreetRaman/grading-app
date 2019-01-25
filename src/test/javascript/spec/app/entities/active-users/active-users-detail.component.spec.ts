/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GradingAppTestModule } from '../../../test.module';
import { ActiveUsersDetailComponent } from 'app/entities/active-users/active-users-detail.component';
import { ActiveUsers } from 'app/shared/model/active-users.model';

describe('Component Tests', () => {
    describe('ActiveUsers Management Detail Component', () => {
        let comp: ActiveUsersDetailComponent;
        let fixture: ComponentFixture<ActiveUsersDetailComponent>;
        const route = ({ data: of({ activeUsers: new ActiveUsers(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [GradingAppTestModule],
                declarations: [ActiveUsersDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ActiveUsersDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ActiveUsersDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.activeUsers).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
